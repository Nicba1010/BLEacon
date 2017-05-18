package de.troido.bleacon.advertiser

import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseSettings
import android.os.Handler
import de.troido.bleacon.ble.HandledBleActor
import de.troido.bleacon.ble.obtainAdvertiser
import de.troido.bleacon.config.BleAdData
import de.troido.bleacon.config.BleAdSettings
import de.troido.bleacon.util.dLog
import kotlin.properties.Delegates


// TODO fix pause/runFor when data changing

class DynamicBleAdvertiser(
        settings: BleAdSettings = BleAdSettings(),
        handler: Handler = Handler(),
        private val callback: AdvertiseCallback = defaultCallback
) : HandledBleActor(handler) {

    companion object {
        internal val defaultCallback = object : AdvertiseCallback() {
            override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) =
                    dLog("advertising successfully started!")

            override fun onStartFailure(errorCode: Int) =
                    dLog("advertising failed with error = $errorCode!")
        }
    }

    private val advertiser = obtainAdvertiser()

    private val adSettings = settings.settings

    var data: BleAdData by Delegates.observable(BleAdData {}) { _, _, n ->
        stop()
        start()
    }

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
