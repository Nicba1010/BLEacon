package de.troido.bleacon.scanner

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.os.Handler
import de.troido.bleacon.ble.HandledBleActor
import de.troido.bleacon.ble.NORDIC_ID
import de.troido.bleacon.ble.obtainScanner
import de.troido.bleacon.config.BleFilter
import de.troido.bleacon.config.BleScanSettings
import de.troido.bleacon.data.BleDeserializer

typealias OnBeaconFound<T> = (BeaconScanner<T>, BluetoothDevice, T) -> Unit

/**
 * @param[handler] optional handler for sharing with other asynchronous actions.
 */
class BeaconScanner<out T>(
        private val deserializer: BleDeserializer<T>,
        filter: BleFilter,
        settings: BleScanSettings = BleScanSettings {},
        handler: Handler = Handler(),
        private val onDeviceFound: OnBeaconFound<T>
) : HandledBleActor(handler) {

    private val scanner = obtainScanner()
    private val filters = listOf(filter.filter)
    private val scanSettings = settings.settings

    private val callback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            result?.run {
                scanRecord
                        ?.getManufacturerSpecificData(NORDIC_ID)
                        ?.let(filter.dataTransform)
                        ?.takeIf { it.size >= deserializer.length }
                        ?.let(deserializer::deserialize)
                        ?.let { onDeviceFound(this@BeaconScanner, device, it) }
            }
        }
    }

    override fun start() {
        handler.post {
            scanner.startScan(filters, scanSettings, callback)
        }
    }

    override fun stop() {
        handler.post { scanner.stopScan(callback) }
    }
}
