package de.troido.bleacon.reverse

import android.bluetooth.BluetoothDevice
import android.os.Handler
import de.troido.bleacon.advertiser.BleAdvertiser
import de.troido.bleacon.ble.HandledBleActor
import de.troido.bleacon.ble.Uuid16
import de.troido.bleacon.config.BleAdData
import de.troido.bleacon.config.BleFilter
import de.troido.bleacon.data.BleDeserializer
import de.troido.bleacon.scanner.BleaconScanner
import java.util.*

class ReverseBleacon<out T>(
        deserializer: BleDeserializer<T>,
        private val uuid16: Uuid16? = null,
        private val uuid128: UUID? = null,
        scanMode: Int? = null,
        advertiseMode: Int? = null,
        txPowerLevel: Int? = null,
        handler: Handler = Handler(),
        onDeviceFound: (BleaconScanner<T>, BluetoothDevice, T) -> Unit
) : HandledBleActor(handler) {

    private val scanner = BleaconScanner(
            BleFilter {
                uuid16 = this@ReverseBleacon.uuid16
                uuid128 = this@ReverseBleacon.uuid128
            },
            deserializer,
            scanMode,
            handler,
            onDeviceFound = onDeviceFound
    )

    private val advertiser = BleAdvertiser(
            BleAdData {
                uuid16 = this@ReverseBleacon.uuid16
                uuid128 = this@ReverseBleacon.uuid128
            },
            advertiseMode,
            txPowerLevel,
            handler
    )

    override fun start() {
        handler.post {
            scanner.start()
            advertiser.start()
        }
    }

    override fun stop() {
        handler.post {
            scanner.stop()
            advertiser.stop()
        }
    }
}
