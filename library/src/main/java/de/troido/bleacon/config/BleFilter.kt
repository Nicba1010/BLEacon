package de.troido.bleacon.config

import android.bluetooth.le.ScanFilter
import android.os.ParcelUuid
import de.troido.bleacon.ble.NORDIC_ID
import de.troido.bleacon.util.Uuid16
import de.troido.bleacon.util.bytes
import java.util.UUID

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
class BleFilter @JvmOverloads constructor(
        uuid16: Uuid16? = null,
        uuid128: UUID? = null,
        address: String? = null,
        name: String? = null,
        build: BleFilter.Builder.() -> Unit = {}
) {
    internal val filter: ScanFilter
    internal val dataTransform: (ByteArray) -> ByteArray

    init {
        val filterBuilder = ScanFilter.Builder().apply {
            address?.let(this::setDeviceAddress)
            name?.let(this::setDeviceName)
        }
        val builder = BleFilter.Builder(filterBuilder).apply {
            uuid16?.let {
                manufacturerData[NORDIC_ID, UUID16_MASK] = it.bytes
                dataTransform = UUID16_TRANSFORM
            }
            uuid128?.let {
                manufacturerData[NORDIC_ID, UUID128_MASK] = it.bytes
                dataTransform = UUID128_TRANSFORM
            }
            build(this)
        }
        dataTransform = builder.dataTransform
        filter = filterBuilder.build()
    }

    class Builder(filter: ScanFilter.Builder) {
        val manufacturerData = BleFilter.ManufacturerDataBuilder(filter)
        val serviceData = BleFilter.ServiceDataBuilder(filter)

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

        var uuid: ParcelUuid? = null
            set(value) {
                if (value != null) {
                    field = value
                    setServiceUuid(value, mask)
                }
            }

        var mask: ParcelUuid? = null
            set(value) {
                field = value
                uuid?.let { setServiceUuid(it, value) }
            }

        operator fun set(uuid: UUID, mask: ByteArray = EMPTY, data: ByteArray) =
                set(ParcelUuid(uuid), mask, data)

        operator fun set(uuid: ParcelUuid, mask: ByteArray = EMPTY, data: ByteArray) {
            filter.setServiceData(uuid, mask, data)
        }

        private fun setServiceUuid(uuid: ParcelUuid, mask: ParcelUuid?) =
                if (mask != null) filter.setServiceUuid(uuid, mask)
                else filter.setServiceUuid(uuid)
    }
}
