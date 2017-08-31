package de.troido.bleacon.scanner

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothProfile
import java.util.UUID

interface BleScanCallback {
    fun onConnect() = Unit
    fun onDisconnect(scanner: BleScanner) = Unit
    fun onWriterReady(writer: BleChrWriter) = Unit
}

/**
 * Simplified implementation of [BleScanCallback] with only the [onWriterReady] method implemented.
 */
class OnWriterReady(private val f: (BleChrWriter) -> Unit) : BleScanCallback {
    override fun onWriterReady(writer: BleChrWriter) = f(writer)
}

/**
 * Returns a [BluetoothGattCallback] representation of `this` [BleScanCallback] for the given
 * service and characteristic UUIDs.
 *
 * @param[svcUuid] service UUID.
 * @param[chrUuid] characteristic UUID.
 */
internal fun BleScanCallback.toBtGattCallback(
        svcUuid: UUID,
        chrUuid: UUID,
        scanner: BleScanner
): BluetoothGattCallback =

        object : BluetoothGattCallback() {

            override fun onConnectionStateChange(gatt: BluetoothGatt?,
                                                 status: Int,
                                                 newState: Int) {
                super.onConnectionStateChange(gatt, status, newState)

                when (newState) {
                    BluetoothProfile.STATE_CONNECTED    -> {
                        gatt?.discoverServices()
                        onConnect()
                    }
                    BluetoothProfile.STATE_DISCONNECTED -> {
                        gatt?.close()
                        onDisconnect(scanner)
                    }
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
                super.onServicesDiscovered(gatt, status)

                gatt?.getService(svcUuid)
                        ?.getCharacteristic(chrUuid)
                        ?.let { chr ->
                            chr.writeType = BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
                            onWriterReady(QueuedBleChrWriter(chr, gatt))
                        }
            }
        }
