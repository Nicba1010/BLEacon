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
    private val queue = ConcurrentLinkedQueue<WriterAction>()

    private val worker = thread {
        while (true) {
            queue.poll()?.let {
                when (it) {
                    is Write -> {
                        chr.value = it.value
                        while (!gatt.writeCharacteristic(chr)) Unit
                    }
                    is Close -> gatt.close()
                }
            }
        }
    }

    override fun write(value: ByteArray) {
        queue.offer(Write(value))
    }

    override fun close() {
        queue.offer(Close)
    }
}

private sealed class WriterAction

@Suppress("ArrayInDataClass")
private data class Write(val value: ByteArray) : WriterAction()

private object Close : WriterAction()
