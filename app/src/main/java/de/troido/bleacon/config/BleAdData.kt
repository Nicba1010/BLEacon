package de.troido.bleacon.config

import android.bluetooth.le.AdvertiseData
import android.os.ParcelUuid
import de.troido.bleacon.ble.NORDIC_ID
import de.troido.bleacon.util.Building
import de.troido.bleacon.util.Uuid16
import de.troido.bleacon.util.bytes
import java.util.*

private val HEADER = ByteArray(1) { -1 }

/**
 * DSL for [AdvertiseData].
 */
class BleAdData(internal val data: AdvertiseData) {

    constructor(builder: BleAdData.Builder) :
            this(builder.data.build())

    constructor(build: BleAdData.Builder.() -> Unit) :
            this(BleAdData.Builder().apply(build))

    class Builder {
        internal val data = AdvertiseData.Builder()

        val manufacturerData = BleAdData.Builder.ManufacturerDataBuilder(data)
        val serviceData = BleAdData.Builder.ServiceDataBuilder(data)

        /** See [AdvertiseData.Builder.setIncludeTxPowerLevel]. */
        var includeTxPowerLevel: Boolean? by Building(data::setIncludeTxPowerLevel)

        /** See [AdvertiseData.Builder.setIncludeDeviceName]. */
        var includeDeviceName: Boolean? by Building(data::setIncludeDeviceName)

        /**
         * Adds the given UUID to the manufacturer data, preceded by the header (`0xFF`).
         * Overrides [uuid128] if set after setting [uuid128].
         */
        var uuid16: Uuid16? by Building { manufacturerData[NORDIC_ID] = HEADER + it.bytes; Unit }

        /**
         * Adds the given UUID to the manufacturer data, preceded by the header (`0xFF`).
         * Overrides [uuid16] if set after setting [uuid16].
         */
        var uuid128: UUID? by Building { manufacturerData[NORDIC_ID] = HEADER + it.bytes; Unit }

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
}
