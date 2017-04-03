package de.troido.bleacon.config

import android.bluetooth.le.AdvertiseData
import de.troido.bleacon.ble.Uuid16
import java.util.*

private val HEADER = byteArrayOf(-1)

class BleAdData(internal val data: android.bluetooth.le.AdvertiseData) {

    constructor(builder: de.troido.bleacon.config.BleAdData.Builder) : this(builder.data.build())
    constructor(build: de.troido.bleacon.config.BleAdData.Builder.() -> Unit) : this(
            de.troido.bleacon.config.BleAdData.Builder().apply(build))

    class Builder {
        internal val data = android.bluetooth.le.AdvertiseData.Builder()
        val manufacturerData = de.troido.bleacon.config.BleAdData.Builder.ManufacturerDataBuilder(
                data)

        var includeTxPowerLevel: Boolean? = null
            set(value) {
                (value ?: false).let(data::setIncludeDeviceName)
            }

        var includeDeviceName: Boolean? = null
            set(value) {
                (value ?: false).let(data::setIncludeDeviceName)
            }

        var uuid16: Uuid16? = null
            set(value) {
                value?.bytes?.let { manufacturerData[NORDIC_ID] = HEADER + it }
            }

        var uuid128: UUID? = null
            set(value) {
                value?.bytes?.let { manufacturerData[NORDIC_ID] = HEADER + it }
            }

        class ManufacturerDataBuilder(private val advertiseData: AdvertiseData.Builder) {
            operator fun set(id: Int, data: ByteArray) {
                advertiseData.addManufacturerData(id, data)
            }
        }
    }
}
