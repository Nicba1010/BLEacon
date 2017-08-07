package de.troido.bleacon.scanner

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.util.Log
import de.troido.bleacon.util.toHex

class AutoCloseBleChrWriter(
        private val chr: BluetoothGattCharacteristic,
        private val gatt: BluetoothGatt
) {

    fun write(value: ByteArray) {
        Log.d("WRITING", "WRITING ${value.toHex()}")

        chr.value = value
        while (!gatt.writeCharacteristic(chr)) Unit

        Log.d("WRITTEN", "WRITTEN ${value.toHex()}")

        gatt.close()
    }
}
