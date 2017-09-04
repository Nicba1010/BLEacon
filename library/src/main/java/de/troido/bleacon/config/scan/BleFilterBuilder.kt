package de.troido.bleacon.config.scan

import android.bluetooth.le.ScanFilter
import android.os.ParcelUuid
import de.troido.ekstend.collections.EMPTY
import kotlin.experimental.or

class BleFilterBuilder(filterBuilder: ScanFilter.Builder) {
    val msd = MsdBuilder()
    val serviceData = ServiceDataBuilder(filterBuilder)

    class MsdBuilder {
        private val builderData = mutableMapOf<Int, MutableList<MsdPart>>()

        internal val data: List<Msd>
            get() = builderData.map { (id, parts) ->
                combineMsdParts(id, parts)
            }

        operator fun set(id: Int, mask: ByteArray = EMPTY, data: ByteArray) =
                builderData.getOrPut(id, ::mutableListOf).add(MsdPart(mask, data))
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
