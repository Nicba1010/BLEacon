package de.troido.bleacon.config

import android.bluetooth.le.AdvertiseData
import android.os.ParcelUuid
import de.troido.bleacon.ble.NORDIC_ID
import de.troido.bleacon.util.Uuid16
import de.troido.bleacon.util.bytes
import java.util.UUID

private val HEADER = ByteArray(1) { -1 }

/**
 * DSL for [AdvertiseData].
 */
class BleAdData
@JvmOverloads constructor(
        uuid16: Uuid16? = null,
        uuid128: UUID? = null,
        includeTxPowerLevel: Boolean? = null,
        includeDeviceName: Boolean? = false,
        build: BleAdData.Builder.() -> Unit = {}
) {
    internal val data = AdvertiseData.Builder().apply {
        includeTxPowerLevel?.let(this::setIncludeTxPowerLevel)
        includeDeviceName?.let(this::setIncludeDeviceName)
        BleAdData.Builder(this).apply {
            uuid16?.let { manufacturerData[NORDIC_ID] = HEADER + it.bytes }
            uuid128?.let { manufacturerData[NORDIC_ID] = HEADER + it.bytes }
            build(this)
        }
    }.build()

    class Builder(internal val data: AdvertiseData.Builder) {
        val manufacturerData = BleAdData.ManufacturerDataBuilder(data)
        val serviceData = BleAdData.ServiceDataBuilder(data)
    }

    /**
     * Manufacturer specific data DSL.
     *
     * Example usage:
     * ```
     * val manufacturerData: ManufacturerDataBuilder = ...
     * val manufacturerId: Int = ...
     * val data: ByteArray = ...
     *
     * // Add manufacturer specific data:
     * manufacturerData[manufacturerId] = data
     * ```
     */
    class ManufacturerDataBuilder(private val advertiseData: AdvertiseData.Builder) {
        operator fun set(id: Int, data: ByteArray) {
            advertiseData.addManufacturerData(id, data)
        }
    }

    /**
     * Service data DSL.
     *
     * Example usage:
     * ```
     * val serviceData: ServiceDataBuilder = ...
     * val uuid: UUID = ...
     * val data: ByteArray = ...
     *
     * // Add the service UUID to advertise data:
     * serviceData += uuid
     * // alternatively:
     * serviceData += ParcelUuid(uuid)
     *
     * // Add service data:
     * serviceData[uuid] = data
     * // alternatively:
     * serviceData[ParcelUuid(uuid)] = data
     * ```
     */
    class ServiceDataBuilder(private val advertiseData: AdvertiseData.Builder) {
        operator fun plusAssign(uuid: ParcelUuid) {
            advertiseData.addServiceUuid(uuid)
        }

        operator fun plusAssign(uuid: UUID) = plusAssign(ParcelUuid(uuid))

        operator fun set(uuid: ParcelUuid, data: ByteArray) {
            advertiseData.addServiceData(uuid, data)
        }

        operator fun set(uuid: UUID, data: ByteArray) = set(ParcelUuid(uuid), data)
    }
}
