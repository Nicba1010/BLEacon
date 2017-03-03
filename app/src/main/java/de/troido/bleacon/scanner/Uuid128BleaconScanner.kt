package de.troido.bleacon.scanner

import android.bluetooth.BluetoothDevice
import de.troido.bleacon.BleaconData
import de.troido.bleacon.util.filterByManufacturerData
import de.troido.bleacon.util.toBytes
import java.util.*

private val MASK = byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1)

class Uuid128BleaconScanner(
        uuid: UUID,
        onDeviceFound: (BleaconScanner, BluetoothDevice, List<BleaconData>) -> Unit
) : BleaconScanner(
        listOf(filterByManufacturerData(NORDIC_ID, uuid.toBytes(), MASK)),
        onDeviceFound
) {
    override fun deserialize(payload: ByteArray): List<BleaconData> =
            listOf(BleaconData.Confirmation)
}
