package de.troido.bleacon.advertiser

import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseSettings
import android.os.Handler
import de.troido.bleacon.ble.HandledBleActor
import de.troido.bleacon.ble.obtainAdvertiser
import de.troido.bleacon.util.BleAdData
import de.troido.bleacon.util.log

private val header = byteArrayOf(-1)

class BleAdvertiser(
        private val data: BleAdData,
        advertiseMode: Int? = null,
        txPowerLevel: Int? = null,
        handler: Handler = Handler()
) : HandledBleActor(handler) {

    private val advertiser = obtainAdvertiser()


    private val callback = object : AdvertiseCallback() {
        override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) =
                log("advertising successfully started!")

        override fun onStartFailure(errorCode: Int) =
                log("advertising failed with error = $errorCode!")
    }

    private val settings = AdvertiseSettings.Builder()
            .setConnectable(false)
            .apply {
                advertiseMode?.let(this::setAdvertiseMode)
                txPowerLevel?.let(this::setTxPowerLevel)
            }
            .build()

    override fun start() {
        handler.post {
            log("starting advertising...")
            advertiser.startAdvertising(settings, data.data, data.data, callback)
        }
    }

    override fun stop() {
        handler.post {
            log("stopping advertising...")
            advertiser.stopAdvertising(callback)
        }
    }
}
