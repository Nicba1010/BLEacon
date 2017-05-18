package de.troido.bleacon.config

import android.bluetooth.le.ScanFilter
import android.os.ParcelUuid
import de.troido.bleacon.util.Building
import de.troido.bleacon.ble.NORDIC_ID
import de.troido.bleacon.util.Uuid16
import de.troido.bleacon.util.bytes
import java.util.*

private val EMPTY = ByteArray(0)

private val UUID16_MASK = ByteArray(2) { -1 }
private val UUID128_MASK = ByteArray(16) { -1 }

private val UUID16_TRANSFORM: (ByteArray) -> ByteArray =
        { it.copyOfRange(UUID16_MASK.size, it.size) }
private val UUID128_TRANSFORM: (ByteArray) -> ByteArray =
        { it.copyOfRange(UUID128_MASK.size, it.size) }

/**
 * DSL for [ScanFilter].
 *
 * @property[dataTransform] Data transformation to be applied after obtaining the data.
 * This is useful for e.g. removing UUIDs before parsing at a higher level, so that the said
 * higher level can remain agnostic to UUID type in use, various headers, etc.
 */
class BleFilter(internal val filter: ScanFilter,
                internal val dataTransform: (ByteArray) -> ByteArray) {

    constructor(builder: Builder) :
            this(builder.filter.build(), builder.dataTransform)

    constructor(build: Builder.() -> Unit) :
            this(BleFilter.Builder().apply(build))

    class Builder {
        internal val filter = ScanFilter.Builder()

        val manufacturerData = ManufacturerDataBuilder(filter)
        val serviceData = ServiceDataBuilder(filter)

        /** See [ScanFilter.Builder.setDeviceAddress]. */
        var address: String? by Building(filter::setDeviceAddress)

        /** See [ScanFilter.Builder.setDeviceName]. */
        var name: String? by Building(filter::setDeviceName)

        /**
         * Adds the given UUID to manufacturer specific data.
         * Overrides [uuid128] if set after setting [uuid128].
         */
        var uuid16: Uuid16? by Building {
            manufacturerData[NORDIC_ID, UUID16_MASK] = it.bytes
            dataTransform = UUID16_TRANSFORM
            Unit
        }

        /**
         * Adds the given UUID to manufacturer specific data.
         * Overrides [uuid16] if set after setting [uuid16].
         */
        var uuid128: UUID? by Building {
            manufacturerData[NORDIC_ID, UUID128_MASK] = it.bytes
            dataTransform = UUID128_TRANSFORM
            Unit
        }

        internal var dataTransform: (ByteArray) -> ByteArray = { it }
    }

    /**
     * Manufacturer specific data DSL.
     *
     * Example usage:
     * ```
     * val manufacturerData: ManufacturerDataBuilder = ...
     * val manufacturerId: Int = ...
     * val mask: ByteArray = ...
     * val data: ByteArray = ...
     *
     * // Add manufacturer specific data:
     * manufacturerData[manufacturerId] = data
     * // Add manufacturer specific data with a mask:
     * manufacturerData[manufacturerId, mask] = data
     * ```
     */
    class ManufacturerDataBuilder(private val filter: ScanFilter.Builder) {
        operator fun set(id: Int, mask: ByteArray = EMPTY, data: ByteArray) {
            filter.setManufacturerData(id, data, mask)
        }
    }

    /**
     * Service data DSL.
     *
     * Example usage:
     * ```
     * val serviceData: ServiceDataBuilder = ...
     *
     * val uuid: UUID = ...
     * val mask: ByteArray = ...
     * val data: ByteArray = ...
     * val uuidMask: UUID = ...
     *
     * // Add service UUID:
     * serviceData.uuid = ParcelUuid(uuid)
     * // optionally add a mask:
     * serviceData.uuidMask = ParcelUuid(uuidMask)
     *
     * // Add service data:
     * serviceData[uuid] = data
     * // alternatively:
     * serviceData[ParcelUuid(uuid)] = data
     *
     * // Add service data with a mask:
     * serviceData[uuid, mask] = data
     * // alternatively:
     * serviceData[ParcelUuid(uuid), mask] = data
     * ```
     */
    class ServiceDataBuilder(private val filter: ScanFilter.Builder) {

        private var _uuid: ParcelUuid? = null
        private var _mask: ParcelUuid? = null

        /** See [ScanFilter.Builder.setServiceUuid]. */
        var uuid: ParcelUuid? by Building {
            _uuid = it
            setServiceUuid(it, _mask)
        }

        /**
         * See [ScanFilter.Builder.setServiceUuid].
         * [uuid] must also be set for the mask to be applied.
         */
        var mask: ParcelUuid? by Building { mask ->
            _mask = mask
            _uuid?.let { uuid -> setServiceUuid(uuid, mask) }
        }

        operator fun set(uuid: UUID, mask: ByteArray = EMPTY, data: ByteArray) =
                set(ParcelUuid(uuid), mask, data)

        operator fun set(uuid: ParcelUuid, mask: ByteArray = EMPTY, data: ByteArray) {
            filter.setServiceData(uuid, mask, data)
        }

        private fun setServiceUuid(uuid: ParcelUuid, mask: ParcelUuid?) =
                filter.setServiceUuid(uuid, mask)
    }
}
