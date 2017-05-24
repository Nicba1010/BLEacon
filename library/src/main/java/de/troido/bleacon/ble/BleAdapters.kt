package de.troido.bleacon.ble

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeAdvertiser
import android.bluetooth.le.BluetoothLeScanner

private const val OBTAIN_DELAY = 50L

/** Returns an enabled [BluetoothAdapter]. */
private val adapter: BluetoothAdapter
    get() = BluetoothAdapter.getDefaultAdapter().apply { if (!isEnabled) enable() }

internal fun obtainAdvertiser(): BluetoothLeAdvertiser {
    while (true) {
        adapter.bluetoothLeAdvertiser?.let { return it }
        Thread.sleep(OBTAIN_DELAY)
    }
}

internal fun obtainScanner(): BluetoothLeScanner {
    while (true) {
        adapter.bluetoothLeScanner?.let { return it }
        Thread.sleep(OBTAIN_DELAY)
    }
}
