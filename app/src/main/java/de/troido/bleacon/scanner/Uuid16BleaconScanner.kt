package de.troido.bleacon.scanner

import android.bluetooth.BluetoothDevice
import de.troido.bleacon.BleaconData
import de.troido.bleacon.util.filterByManufacturerData

private val MASK = byteArrayOf(-1, -1)
private val OFFSET = MASK.size

class Uuid16BleaconScanner(
        uuid16: Uuid16,
        onDeviceFound: (BleaconScanner, BluetoothDevice, List<BleaconData>) -> Unit
) : BleaconScanner(
        listOf(filterByManufacturerData(NORDIC_ID, uuid16.bytes, MASK)),
        onDeviceFound
) {

    override fun deserialize(payload: ByteArray): List<BleaconData> {
        val data = mutableListOf<BleaconData>()

        try {
            var i = OFFSET
            while (i < payload.size) {
                when (payload[i]) {
                    BleaconData.AnalogInput.ID -> {
                        data += BleaconData.AnalogInput(payload[i + 1])
                        i += 2
                    }
                    else                       -> return data.toList()
                }
            }
        } catch (ignored: ArrayIndexOutOfBoundsException) {}

        return data.toList()
    }
}
