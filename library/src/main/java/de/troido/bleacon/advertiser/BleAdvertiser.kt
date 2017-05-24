package de.troido.bleacon.advertiser

import android.bluetooth.le.AdvertiseCallback
import android.os.Handler
import de.troido.bleacon.ble.HandledBleActor
import de.troido.bleacon.config.BleAdData
import de.troido.bleacon.config.BleAdSettings

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
        private val callback: AdvertiseCallback = DynamicBleAdvertiser.defaultCallback
) : HandledBleActor(handler) {

    private val advertiser = DynamicBleAdvertiser(settings, handler, callback)

    override fun start() = advertiser.start()

    override fun stop() = advertiser.stop()
}
