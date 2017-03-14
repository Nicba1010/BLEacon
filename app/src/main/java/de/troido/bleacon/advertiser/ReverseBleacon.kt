package de.troido.bleacon.advertiser

import android.bluetooth.BluetoothDevice
import android.os.Handler
import de.troido.bleacon.ble.HandledBleActor
import de.troido.bleacon.data.BleDeserializer
import de.troido.bleacon.scanner.BleaconScanner
import de.troido.bleacon.scanner.Uuid16
import de.troido.bleacon.util.bleFilter
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
            bleFilter {
                this@ReverseBleacon.uuid16?.let { uuid16 = it }
                this@ReverseBleacon.uuid128.let { uuid128 = it }
            },
            deserializer,
            scanMode,
            handler,
            onDeviceFound = onDeviceFound
    )

    private val advertiser = BleAdvertiser(uuid16, uuid128, advertiseMode, txPowerLevel, handler)

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
