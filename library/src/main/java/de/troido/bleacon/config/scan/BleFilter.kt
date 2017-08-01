package de.troido.bleacon.config.scan

import android.bluetooth.le.ScanFilter
import android.os.ParcelUuid
import de.troido.bleacon.ble.NORDIC_ID
import de.troido.bleacon.util.EMPTY
import de.troido.bleacon.util.Uuid16
import de.troido.bleacon.util.bytes
import java.util.UUID
import kotlin.experimental.or

private val UUID16_MASK = ByteArray(2) { -1 }
private val UUID128_MASK = ByteArray(16) { -1 }

val UUID16_TRANSFORM: (ByteArray) -> ByteArray = { it.copyOfRange(UUID16_MASK.size, it.size) }
val UUID128_TRANSFORM: (ByteArray) -> ByteArray = { it.copyOfRange(UUID128_MASK.size, it.size) }
val IDENTITY: (ByteArray) -> ByteArray = { it }

@JvmOverloads
fun bleFilter(uuid16: Uuid16? = null,
              uuid128: UUID? = null,
              address: String? = null,
              name: String? = null,
              build: BleFilterBuilder.() -> Unit = {}): ScanFilter =

        ScanFilter.Builder().apply {
            name?.let(this::setDeviceName)
            address?.let(this::setDeviceAddress)

            BleFilterBuilder(this).apply {
                uuid16?.let { msd[NORDIC_ID, UUID16_MASK] = it.bytes }
                uuid128?.let { msd[NORDIC_ID, UUID128_MASK] = it.bytes }

                for ((id, mask, data) in msd.data) setManufacturerData(id, mask, data)
                serviceData.uuid?.let { uuid -> setServiceUuid(uuid, serviceData.mask) }
            }.apply(build)
        }.build()


class BleFilterBuilder(filterBuilder: ScanFilter.Builder) {
    val msd = MsdBuilder()
    val serviceData = ServiceDataBuilder(filterBuilder)

    class MsdBuilder {
        private val builderData = mutableMapOf<Int, MutableList<MsdPart>>()

        internal val data: List<Msd>
            get() = builderData.map { (id, parts) -> combineMsdParts(id, parts) }

        operator fun set(id: Int, mask: ByteArray = EMPTY, data: ByteArray) =
                builderData.getOrPut(id, ::mutableListOf).add(
                        MsdPart(mask, data))
    }

    class ServiceDataBuilder(private val filterBuilder: ScanFilter.Builder) {
        var uuid: ParcelUuid? = null
        var mask: ParcelUuid? = null

        operator fun set(uuid: ParcelUuid, mask: ByteArray = EMPTY, data: ByteArray) {
            filterBuilder.setServiceData(uuid, data, mask)
        }
    }
}


private fun combineMsdParts(id: Int, parts: List<MsdPart>): Msd {
    val size = parts.map { it.mask.size }.max()
            ?: return Msd(id, byteArrayOf(), byteArrayOf())
    val msdMask = ByteArray(size)
    val msdData = ByteArray(size)

    for ((mask, data) in parts) {
        for (i in 0..mask.lastIndex) msdMask[i] = msdMask[i] or mask[i]
        for (i in 0..data.lastIndex) msdData[i] = msdData[i] or data[i]
    }

    return Msd(id, msdMask, msdData)
}
