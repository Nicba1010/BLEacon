package de.troido.bleacon.config

import android.bluetooth.le.ScanFilter
import de.troido.bleacon.ble.Uuid16
import de.troido.bleacon.util.NORDIC_ID
import de.troido.bleacon.util.bytes
import java.util.*

private val EMPTY = byteArrayOf()
private val UUID16_MASK = byteArrayOf(-1, -1)
private val UUID128_MASK = byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1,
                                       -1, -1, -1, -1, -1, -1, -1, -1)

private val UUID16_TRANSFORM: (ByteArray) -> ByteArray =
        { it.copyOfRange(UUID16_MASK.size, it.size) }
private val UUID128_TRANSFORM: (ByteArray) -> ByteArray =
        { it.copyOfRange(UUID128_MASK.size, it.size) }

class BleFilter(internal val filter: ScanFilter,
                internal val dataTransform: (ByteArray) -> ByteArray) {

    constructor(builder: Builder) : this(builder.filter.build(), builder.dataTransform)

    constructor(build: Builder.() -> Unit) : this(BleFilter.Builder().apply(build))

    class Builder {
        internal val filter = ScanFilter.Builder()
        val manufacturerData = ManufacturerDataBuilder(filter)

        var address: String? = null
            set(value) {
                value?.let(filter::setDeviceAddress)
            }

        var uuid16: Uuid16? = null
            set(value) {
                value?.let { manufacturerData[NORDIC_ID, UUID16_MASK] = it.bytes }
                dataTransform = UUID16_TRANSFORM
            }

        var uuid128: UUID? = null
            set(value) {
                value?.let { manufacturerData[NORDIC_ID, UUID128_MASK] = it.bytes }
                dataTransform = UUID128_TRANSFORM
            }

        var name: String? = null
            set(value) {
                value?.let(filter::setDeviceName)
            }

        var dataTransform: (ByteArray) -> ByteArray = { it }
    }

    class ManufacturerDataBuilder(private val filter: ScanFilter.Builder) {
        operator fun set(id: Int, mask: ByteArray = EMPTY, data: ByteArray) {
            filter.setManufacturerData(id, data, mask)
        }
    }
}
