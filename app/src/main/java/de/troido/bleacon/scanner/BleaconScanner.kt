package de.troido.bleacon.scanner

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.os.Handler
import de.troido.bleacon.data.BleDeserializer
import de.troido.bleacon.util.BleFilter
import de.troido.bleacon.util.mfilter
import de.troido.bleacon.util.postDelayed

private val SCAN_SETTINGS =
        ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build()

class BleaconScanner<out T>(
        private val filter: BleFilter,
        private val deserializer: BleDeserializer<T>,
        private val onDeviceFound: (BleaconScanner<T>, BluetoothDevice, T) -> Unit
) {
    private val handler = Handler()
    private val scanner = obtainScanner()

    private val callback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            result?.run {
                scanRecord
                        ?.getManufacturerSpecificData(NORDIC_ID)
                        ?.let(filter.dataTransform)
                        ?.mfilter { it.size >= deserializer.length }
                        ?.let(deserializer::deserialize)
                        ?.let { onDeviceFound(this@BleaconScanner, device, it) }
            }
        }
    }

    fun start() = handler.post {
        scanner.startScan(listOf(filter.filter), SCAN_SETTINGS, callback)
    }

    fun stop() = handler.post { scanner.stopScan(callback) }

    fun pause(millis: Long) {
        stop()
        handler.postDelayed(millis) { scanner.startScan(callback) }
    }
}

private fun obtainScanner(): BluetoothLeScanner {
    val adapter = BluetoothAdapter.getDefaultAdapter()
    if (!adapter.isEnabled) {
        adapter.enable()
    }
    while (true) {
        adapter.bluetoothLeScanner?.let { return it }
        Thread.sleep(50)
    }
}
