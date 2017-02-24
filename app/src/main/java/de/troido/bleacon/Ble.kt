package de.troido.bleacon

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.*
import android.os.Handler

private const val MAN_ID = 89

private val MAN_UUID_MASK = byteArrayOf(0, 0, -1, -1, -1, -1)

private val SCAN_SETTINGS =
        ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_POWER).build()

abstract class ContinuousScanner(uuid32: Uuid32) {
    private val handler = Handler()

    private val manUuid = byteArrayOf(0, 0) + uuid32.bytes

    private val filters = mutableListOf(
            ScanFilter.Builder().setManufacturerData(MAN_ID, manUuid, MAN_UUID_MASK).build()
    )

    private val scanner = obtainScanner()

    private val callback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            result?.scanRecord?.getManufacturerSpecificData(MAN_ID)?.let {
                onFound(this@ContinuousScanner, it[0], it.copyOfRange(1, it.size))
            }
        }
    }

    fun start() = handler.post { scanner.startScan(filters, SCAN_SETTINGS, callback) }

    fun stop() = handler.post { scanner.stopScan(callback) }

    fun pause(millis: Long) {
        stop()
        handler.postDelayed(millis) { scanner.startScan(callback) }
    }

    abstract fun onFound(scanner: ContinuousScanner, type: Byte, data: ByteArray): Unit
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
