package de.troido.bleacon.beaconno

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothProfile
import de.troido.bleacon.scanner.BleChrWriter
import de.troido.bleacon.scanner.OneTimeBleChrWriter
import java.util.UUID

typealias OnBeaconno<T> = (scanner: BeaconnoScanner<T>, device: BeaconnoDevice<T>) -> Unit
typealias OnBeaconnoData<T> = (data: T) -> Unit
typealias OnBeaconnoWriter = (writer: BleChrWriter) -> Unit

interface OnBeaconnoConnection {
    fun onConnect() {}
    fun onDisconnect() {}
    fun onBeaconnoWriter(writer: BleChrWriter) {}
}

typealias BleChrWriterCtor = (BluetoothGattCharacteristic, BluetoothGatt) -> BleChrWriter

fun bleConnectionCallback(svcUuid: UUID,
                          chrUuid: UUID,
                          writerCtor: BleChrWriterCtor = ::OneTimeBleChrWriter,
                          listener: OnBeaconnoConnection
): BluetoothGattCallback =
        bleConnectionCallback(svcUuid, chrUuid, writerCtor, listener::onConnect,
                              listener::onDisconnect, listener::onBeaconnoWriter)

fun bleConnectionCallback(svcUuid: UUID,
                          chrUuid: UUID,
                          writerCtor: BleChrWriterCtor = ::OneTimeBleChrWriter,
                          onConnect: () -> Unit = {},
                          onDisconnect: () -> Unit = {},
                          onBeaconnoWriter: OnBeaconnoWriter = {}
): BluetoothGattCallback = object : BluetoothGattCallback() {

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
                onDisconnect()
            }
        }
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
        super.onServicesDiscovered(gatt, status)

        gatt?.getService(svcUuid)
                ?.getCharacteristic(chrUuid)
                ?.let { chr ->
                    chr.writeType = BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
                    onBeaconnoWriter(writerCtor(chr, gatt))
                }
    }
}
