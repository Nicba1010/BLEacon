package de.troido.bleacon.scanner

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.concurrent.thread

/**
 * BLE characteristic writer. All writing is executed on a worker thread.
 */
class QueuedBleChrWriter(
        private val chr: BluetoothGattCharacteristic,
        private val gatt: BluetoothGatt
) : BleChrWriter {
    private val queue = ConcurrentLinkedQueue<ByteArray>()

    private val worker = thread {
        while (true) {
            queue.poll()?.let {
                chr.value = it
                while (!gatt.writeCharacteristic(chr)) Unit
            }
        }
    }

    override fun write(value: ByteArray) {
        queue.offer(value)
    }

    override fun close() {
        thread {
            while (true) {
                if (queue.isEmpty()) {
                    gatt.close()
                    break
                }
            }
        }
    }
}
