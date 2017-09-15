package de.troido.bleacon.scanner

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import kotlin.concurrent.thread

class OneTimeBleChrWriter(
        private val chr: BluetoothGattCharacteristic,
        private val gatt: BluetoothGatt
) : BleChrWriter {

    override fun write(value: ByteArray) {
        thread {
            chr.value = value
            while (!gatt.writeCharacteristic(chr)) Unit
        }
    }

    override fun close() = gatt.close()
}
