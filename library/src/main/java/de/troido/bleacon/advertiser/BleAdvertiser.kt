package de.troido.bleacon.advertiser

import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.os.Handler
import de.troido.bleacon.ble.HandledBleActor
import de.troido.bleacon.ble.obtainAdvertiser
import de.troido.bleacon.config.advertise.adSettings
import de.troido.bleacon.config.advertise.bleAdData
import de.troido.ekstend.android.debug.logD

private val defaultCallback = object : AdvertiseCallback() {
    override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) =
            logD("advertising successfully started!")

    override fun onStartFailure(errorCode: Int) =
            logD("advertising failed with error = $errorCode!")
}

/**
 * Idiomatic wrapper for [android.bluetooth.le.BluetoothLeAdvertiser].
 *
 * @param[handler] optional handler for sharing with other asynchronous actions.
 *
 * @param[callback] optional custom callback. Default callback logs results to debug log.
 */
class BleAdvertiser
@JvmOverloads constructor(
        private val data: AdvertiseData,
        private val settings: AdvertiseSettings = adSettings(),
        private val dataMode: BleAdDataMode = BleAdDataMode.NO_SCAN_RESPONSE,
        handler: Handler = Handler(),
        private val callback: AdvertiseCallback = defaultCallback
) : HandledBleActor(handler) {

    private val advertiser = obtainAdvertiser()

    override fun start() {
        handler.post {
            logD("starting advertising...")
            when (dataMode) {
                BleAdDataMode.SCAN_RESPONSE       ->
                    advertiser.startAdvertising(settings, data, data, callback)
                BleAdDataMode.EMPTY_SCAN_RESPONSE ->
                    advertiser.startAdvertising(settings, data, bleAdData(), callback)
                BleAdDataMode.NO_SCAN_RESPONSE    ->
                    advertiser.startAdvertising(settings, data, callback)
            }
        }
    }

    override fun stop() {
        handler.post {
            logD("stopping advertising...")
            advertiser.stopAdvertising(callback)
        }
    }
}
