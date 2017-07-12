package de.troido.bleacon.advertiser

import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseSettings
import android.os.Handler
import de.troido.bleacon.ble.HandledBleActor
import de.troido.bleacon.ble.obtainAdvertiser
import de.troido.bleacon.config.BleAdData
import de.troido.bleacon.config.BleAdSettings
import de.troido.bleacon.util.dLog

private val defaultCallback = object : AdvertiseCallback() {
    override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) =
            dLog("advertising successfully started!")

    override fun onStartFailure(errorCode: Int) =
            dLog("advertising failed with error = $errorCode!")
}

/**
 * Idiomatic wrapper for [android.bluetooth.le.BluetoothLeAdvertiser].
 *
 * @param[handler] optional handler for sharing with other asynchronous actions.
 *
 * @param[callback] optional custom callback. Default callback logs results to debug log.
 */
class BleAdvertiser(
        private val data: BleAdData,
        settings: BleAdSettings = BleAdSettings(),
        handler: Handler = Handler(),
        private val callback: AdvertiseCallback = defaultCallback
) : HandledBleActor(handler) {

    private val advertiser = obtainAdvertiser()

    private val adSettings = settings.settings

    override fun start() {
        handler.post {
            dLog("starting advertising...")
            advertiser.startAdvertising(adSettings, data.data, data.data, callback)
        }
    }

    override fun stop() {
        handler.post {
            dLog("stopping advertising...")
            advertiser.stopAdvertising(callback)
        }
    }
}
