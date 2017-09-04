package de.troido.bleacon.config.advertise

import android.bluetooth.le.AdvertiseData
import android.os.ParcelUuid

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
