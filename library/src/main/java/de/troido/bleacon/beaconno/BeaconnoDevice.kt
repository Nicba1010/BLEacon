package de.troido.bleacon.beaconno

import android.bluetooth.BluetoothGattCallback
import android.bluetooth.le.ScanRecord
import android.content.Context
import de.troido.bleacon.ble.NORDIC_ID
import de.troido.bleacon.data.BleDeserializer
import de.troido.bleacon.scanner.BeaconMetaData

class BeaconnoDevice<out T>(private val context: Context,
                            private val deserializer: BleDeserializer<T>,
                            private val dataTransform: (ByteArray) -> ByteArray,
                            private val scanRecord: ScanRecord,
                            val metaData: BeaconMetaData) {

    val data: T? by lazy {
        scanRecord.getManufacturerSpecificData(NORDIC_ID)
                ?.let(dataTransform)
                ?.let {
                    when (deserializer.length) {
                        BleDeserializer.ALL -> it
                        in 0..it.size       -> it.copyOfRange(0, deserializer.length)
                        else                -> null
                    }
                }
                ?.let(deserializer::deserialize)
    }

    fun connect(context: Context, callback: BluetoothGattCallback) {
        metaData.device.connectGatt(context, false, callback)
    }
}
