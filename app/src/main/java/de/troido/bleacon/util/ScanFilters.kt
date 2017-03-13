package de.troido.bleacon.util

import android.bluetooth.le.ScanFilter
import de.troido.bleacon.scanner.Uuid16
import java.util.*

private val EMPTY = byteArrayOf()
private val UUID16_MASK = byteArrayOf(-1, -1)
private val UUID128_MASK = byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1,
                                       -1, -1, -1, -1, -1, -1, -1, -1)

private val UUID16_TRANSFORM: (ByteArray) -> ByteArray =
        { it.copyOfRange(UUID16_MASK.size, it.size) }
private val UUID128_TRANSFORM: (ByteArray) -> ByteArray =
        { it.copyOfRange(UUID128_MASK.size, it.size) }

fun bleFilter(build: BleFilter.Builder.() -> Unit): BleFilter =
        BleFilter.Builder().apply(build).let { BleFilter(it.filter.build(), it.dataTransform) }

class BleFilter(val filter: ScanFilter, val dataTransform: (ByteArray) -> ByteArray) {
    class Builder {
        internal val filter = ScanFilter.Builder()
        val manufacturerData = ManufacturerDataBuilder(filter)

        var address: String? = null
            set(value) {
                value?.let(filter::setDeviceAddress)
            }

        var uuid16: Uuid16? = null
            set(value) {
                value?.let { filter.setManufacturerData(NORDIC_ID, it.bytes, UUID16_MASK) }
                dataTransform = UUID16_TRANSFORM
            }

        var uuid128: UUID? = null
            set(value) {
                value?.let { filter.setManufacturerData(NORDIC_ID, it.bytes, UUID128_MASK) }
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
