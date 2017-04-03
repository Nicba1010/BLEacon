package de.troido.bleacon.scanner

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.os.Handler
import de.troido.bleacon.ble.HandledBleActor
import de.troido.bleacon.ble.obtainScanner
import de.troido.bleacon.config.BleFilter
import de.troido.bleacon.data.BleDeserializer
import de.troido.bleacon.util.NORDIC_ID

private val EMPTY = byteArrayOf()
private val UUID16_MASK = byteArrayOf(-1, -1)
private val UUID128_MASK = byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1,
                                       -1, -1, -1, -1, -1, -1, -1, -1)

private val UUID16_TRANSFORM: (ByteArray) -> ByteArray =
        { it.copyOfRange(UUID16_MASK.size, it.size) }
private val UUID128_TRANSFORM: (ByteArray) -> ByteArray =
        { it.copyOfRange(UUID128_MASK.size, it.size) }

class BleaconScanner<out T>(
        private val filter: BleFilter,
        private val deserializer: BleDeserializer<T>,
        scanMode: Int? = null,
        handler: Handler = Handler(),
        private val onDeviceFound: (BleaconScanner<T>, BluetoothDevice, T) -> Unit
) : HandledBleActor(handler) {

    private val scanner = obtainScanner()
    private val scanSettings = ScanSettings.Builder()
            .apply { scanMode?.let(this::setScanMode) }
            .build()

    private val callback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            result?.run {
                scanRecord
                        ?.getManufacturerSpecificData(NORDIC_ID)
                        ?.let(filter.dataTransform)
                        ?.takeIf { it.size >= deserializer.length }
                        ?.let(deserializer::deserialize)
                        ?.let { onDeviceFound(this@BleaconScanner, device, it) }
            }
        }
    }

    override fun start() {
        handler.post {
            scanner.startScan(listOf(filter.filter), scanSettings, callback)
        }
    }

    override fun stop() {
        handler.post { scanner.stopScan(callback) }
    }
}
