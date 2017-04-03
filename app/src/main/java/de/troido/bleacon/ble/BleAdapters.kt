package de.troido.bleacon.ble

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeAdvertiser
import android.bluetooth.le.BluetoothLeScanner
import de.troido.bleacon.util.logger

private val log = logger("BleAdapters")

private val adapter: BluetoothAdapter
    get() = BluetoothAdapter.getDefaultAdapter().apply { if (!isEnabled) enable() }

internal fun obtainAdvertiser(): BluetoothLeAdvertiser {
    while (true) {
        adapter.bluetoothLeAdvertiser?.let { return it }
        Thread.sleep(50)
    }
}

internal fun obtainScanner(): BluetoothLeScanner {
    while (true) {
        adapter.bluetoothLeScanner?.let { return it }
        Thread.sleep(50)
    }
}
