package de.troido.bleacon.scanner

import de.troido.bleacon.BleaconData
import de.troido.bleacon.util.filterByManufacturerData
import de.troido.bleacon.util.toBytes
import java.util.*

private const val ID = 0

class Uuid128BleaconScanner(
        uuid: UUID,
        onDeviceFound: (BleaconScanner, List<BleaconData>) -> Unit
) : BleaconScanner(
        ID,
        listOf(filterByManufacturerData(ID, uuid.toBytes())),
        onDeviceFound
) {
    override fun deserialize(payload: ByteArray): List<BleaconData> =
            listOf(BleaconData.Confirmation)
}
