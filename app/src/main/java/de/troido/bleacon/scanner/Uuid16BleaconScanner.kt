package de.troido.bleacon.scanner

import android.bluetooth.BluetoothDevice
import de.troido.bleacon.BleaconData
import de.troido.bleacon.util.filterByManufacturerData
import de.troido.bleacon.util.toUInt

class Uuid16BleaconScanner(
        uuid16: Uuid16,
        onDeviceFound: (BleaconScanner, BluetoothDevice, List<BleaconData>) -> Unit
) : BleaconScanner(
        toManufacturerId(uuid16.bytes),
        listOf(filterByManufacturerData(toManufacturerId(uuid16.bytes))),
        onDeviceFound
) {

    override fun deserialize(payload: ByteArray): List<BleaconData> {
        val data = mutableListOf<BleaconData>()

        try {
            var i = 0
            while (i < payload.size) {
                when (payload[i]) {
                    BleaconData.AnalogInput.ID -> {
                        data += BleaconData.AnalogInput(payload[i + 1])
                        i += 2
                    }
                    else                       -> data.toList()
                }
            }
        } catch (ignored: ArrayIndexOutOfBoundsException) {}

        return data.toList()
    }
}

private fun toManufacturerId(data: ByteArray): Int = (data[1].toUInt() shl 8) + data[0].toUInt()
