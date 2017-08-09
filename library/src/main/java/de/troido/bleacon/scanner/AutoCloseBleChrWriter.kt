package de.troido.bleacon.scanner

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.util.Log
import de.troido.bleacon.util.toHex
import kotlin.concurrent.thread

private const val TRIES = 1000

class AutoCloseBleChrWriter(
        private val chr: BluetoothGattCharacteristic,
        private val gatt: BluetoothGatt
) {

    fun write(value: ByteArray) = thread {
        Log.d("WRITING", "WRITING ${value.toHex()}")

        chr.value = value
        for (i in 0..TRIES) {
            if (gatt.writeCharacteristic(chr)) {
                Log.d("WRITTEN", "WRITTEN ${value.toHex()}")
                break
            }
        }

        gatt.close()
    }
}
