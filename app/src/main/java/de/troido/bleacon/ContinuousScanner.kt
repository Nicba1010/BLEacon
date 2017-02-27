package de.troido.bleacon

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.*
import android.os.Handler
import java.util.*

private const val MAN_ID = 89

private val MAN_UUID32_MASK = byteArrayOf(0, 0, -1, -1, -1, -1)
private val MAN_UUID_MASK = byteArrayOf(0, 0, -1, -1, -1, -1, -1, -1, -1, -1,
                                        -1, -1, -1, -1, -1, -1, -1, -1)

private val SCAN_SETTINGS =
        ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_POWER).build()

abstract class ContinuousScanner(private val filters: List<ScanFilter>) {
    private val handler = Handler()
    private val scanner = obtainScanner()

    private val callback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            result?.scanRecord?.bytes?.let { onDeviceFound(it.copyOf()) }
        }
    }

    abstract fun onDeviceFound(data: ByteArray)

    fun start() = handler.post { scanner.startScan(filters, SCAN_SETTINGS, callback) }

    fun stop() = handler.post { scanner.stopScan(callback) }

    fun pause(millis: Long) {
        stop()
        handler.postDelayed(millis) { scanner.startScan(callback) }
    }
}

fun UUID.toBytes(): ByteArray = hexStringToByteArray(toString().replace("-", ""))

class UuidContinuousScanner(
        uuid: UUID,
        private val onFound: (UuidContinuousScanner, Map<Byte, ByteArray>) -> Unit
) : ContinuousScanner(listOf(ScanFilter.Builder()
                                     .setManufacturerData(
                                             MAN_ID,
                                             byteArrayOf(0, 0) + uuid.toBytes(),
                                             MAN_UUID_MASK
                                     )
                                     .build())) {
    override fun onDeviceFound(data: ByteArray) = onFound(this, mapOf()) //TODO parsing
}

class Uuid32ContinuousScanner(
        uuid32: Uuid32,
        private val onFound: (Uuid32ContinuousScanner, Map<Byte, ByteArray>) -> Unit
) : ContinuousScanner(listOf(ScanFilter.Builder()
                                     .setManufacturerData(
                                             MAN_ID,
                                             byteArrayOf(0, 0) + uuid32.bytes,
                                             MAN_UUID32_MASK
                                     )
                                     .build())) {
    override fun onDeviceFound(data: ByteArray) = onFound(this, mapOf()) //TODO parsing
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
