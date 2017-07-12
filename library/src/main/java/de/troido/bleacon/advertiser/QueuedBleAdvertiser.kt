package de.troido.bleacon.advertiser

import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.os.Handler
import de.troido.bleacon.ble.obtainAdvertiser
import de.troido.bleacon.config.BleAdData
import de.troido.bleacon.config.BleAdSettings
import de.troido.bleacon.util.forEachPolled
import de.troido.bleacon.util.postDelayed
import de.troido.bleacon.util.sequence
import java.util.LinkedList
import java.util.PriorityQueue

private const val ADV_TIME = 1 * 60 * 1000L
private const val INITIAL_CAPACITY = 8

class QueuedBleAdvertiser(
        settings: BleAdSettings = BleAdSettings()
) {
    private val handler = Handler()
    private val settings = settings.settings
    private val advertiser = obtainAdvertiser()

    private val outgoing = LinkedList<QueuedAdvertiseCallback>()
    private val waiting = PriorityQueue(
            INITIAL_CAPACITY,
            compareBy(QueuedAdvertiseCallback::timeRemaining)
                    .reversed()
                    .thenBy(QueuedAdvertiseCallback::lastAdTimestamp)
    )

    private var scheduling = false

    operator fun plusAssign(data: BleAdData) = add(data)

    fun add(data: BleAdData): Unit = QueuedAdvertiseCallback(data.data, ADV_TIME, 0).let {
        if (waiting.isNotEmpty()) {
            waiting.add(it)
            return
        }

        advertiser.startAdvertising(settings, it.data, it.data, it)
        handler.postDelayed(ADV_TIME) {
            outgoing -= it
            it.timeRemaining -= System.currentTimeMillis() - it.lastAdTimestamp
            advertiser.stopAdvertising(it)
        }
    }

    private fun organize() {
        scheduling = true
        handler.removeCallbacksAndMessages(null)

        val advTime = advertisingTime()

        outgoing.forEachPolled {
            it.timeRemaining -= System.currentTimeMillis() - it.lastAdTimestamp
            advertiser.stopAdvertising(it)
            if (it.timeRemaining > 0) {
                waiting += it
            }
        }

        if (waiting.isEmpty()) {
            scheduling = false
            return
        }

        val (finishing, remaining) = waiting.sequence()
                .filter { it.timeRemaining > 0 }
                .partition { it.timeRemaining < advTime }

        (finishing + remaining).forEach {
            advertiser.startAdvertising(settings, it.data, it.data, it)
        }

        finishing.forEach {
            handler.postDelayed(it.timeRemaining) {
                advertiser.stopAdvertising(it)
                outgoing -= it
                it.timeRemaining -= System.currentTimeMillis() - it.lastAdTimestamp
            }
        }

        handler.postDelayed(advTime, this::organize)
    }

    private fun advertisingTime(): Long =
            (outgoing.size.toDouble() / (outgoing.size + waiting.size) * ADV_TIME)
                    .toLong()

    private inner class QueuedAdvertiseCallback(
            val data: AdvertiseData,
            var timeRemaining: Long,
            var lastAdTimestamp: Long
    ) : AdvertiseCallback() {
        override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
            lastAdTimestamp = System.currentTimeMillis()
            outgoing += this
        }

        override fun onStartFailure(errorCode: Int) {
            if (errorCode == ADVERTISE_FAILED_TOO_MANY_ADVERTISERS) {
                waiting += this
                if (!scheduling) {
                    organize()
                }
            }
        }

        override fun equals(other: Any?): Boolean =
                this === other || other is QueuedAdvertiseCallback && other.data == data

        override fun hashCode(): Int = data.hashCode()
    }
}
