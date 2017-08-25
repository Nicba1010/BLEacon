package de.troido.bleacon.scanner

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.util.Log
import de.troido.ekstend.serial.toHex
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.concurrent.thread

/**
 * BLE characteristic writer. All writing is executed on a worker thread.
 */
class BleChrWriter(
        private val chr: BluetoothGattCharacteristic,
        private val gatt: BluetoothGatt
) {
    private val queue = ConcurrentLinkedQueue<ByteArray>()

    private val worker = thread {
        while (true) {
            queue.poll()?.let {
                Log.d("WRITING", "WRITING ${it.toHex()}")

                chr.value = it
                while (!gatt.writeCharacteristic(chr)) Unit

                Log.d("WRITTEN", "WRITTEN ${it.toHex()}")
            }
        }
    }

    fun write(value: ByteArray) {
        queue.offer(value)
    }
}
