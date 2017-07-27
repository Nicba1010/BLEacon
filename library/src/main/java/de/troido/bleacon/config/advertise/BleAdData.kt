package de.troido.bleacon.config.advertise

import android.bluetooth.le.AdvertiseData
import android.os.ParcelUuid
import de.troido.bleacon.ble.NORDIC_ID
import de.troido.bleacon.util.Uuid16
import de.troido.bleacon.util.bytes
import java.util.UUID

@JvmOverloads
fun bleAdData(uuid16: Uuid16? = null,
              uuid128: UUID? = null,
              includeTxPowerLevel: Boolean = false,
              includeDeviceName: Boolean = false,
              build: AdDataBuilder.() -> Unit = {}) =

        AdvertiseData.Builder().apply {
            setIncludeTxPowerLevel(includeTxPowerLevel)
            setIncludeDeviceName(includeDeviceName)

            AdDataBuilder(this).apply {
                uuid16?.let { msd[NORDIC_ID] = it.bytes }
                uuid128?.let { msd[NORDIC_ID] = it.bytes }
            }.apply(build)
        }.build()

class AdDataBuilder(dataBuilder: AdvertiseData.Builder) {
    val msd = MsdBuilder(dataBuilder)
    val serviceData = ServiceDataBuilder(dataBuilder)

    class ServiceDataBuilder(private val dataBuilder: AdvertiseData.Builder) {
        val uuids = UuidCollector(dataBuilder)

        operator fun set(uuid: ParcelUuid, data: ByteArray) {
            dataBuilder.addServiceData(uuid, data)
        }
    }

    class MsdBuilder(private val dataBuilder: AdvertiseData.Builder) {
        operator fun set(id: Int, data: ByteArray) {
            dataBuilder.addManufacturerData(id, data)
        }
    }

    class UuidCollector(private val dataBuilder: AdvertiseData.Builder) {
        operator fun plusAssign(uuid: ParcelUuid) {
            dataBuilder.addServiceUuid(uuid)
        }
    }
}
