package de.troido.bleacon.config.scan

import android.bluetooth.le.ScanFilter
import de.troido.bleacon.ble.NORDIC_ID
import de.troido.ekstend.uuid.Uuid16
import de.troido.ekstend.uuid.bytes
import java.util.UUID

private val UUID16_MASK = ByteArray(2) { -1 }
private val UUID128_MASK = ByteArray(16) { -1 }

val UUID16_TRANSFORM: (ByteArray) -> ByteArray = { it.copyOfRange(UUID16_MASK.size, it.size) }
val UUID128_TRANSFORM: (ByteArray) -> ByteArray = { it.copyOfRange(UUID128_MASK.size, it.size) }
val IDENTITY: (ByteArray) -> ByteArray = { it }

@JvmOverloads
fun bleFilter(uuid16: Uuid16? = null,
              uuid128: UUID? = null,
              address: String? = null,
              name: String? = null,
              build: BleFilterBuilder.() -> Unit = {}): ScanFilter =

        ScanFilter.Builder().apply {
            name?.let(this::setDeviceName)
            address?.let(this::setDeviceAddress)

            BleFilterBuilder(this).apply {
                uuid16?.let { msd[NORDIC_ID, UUID16_MASK] = it.bytes }
                uuid128?.let { msd[NORDIC_ID, UUID128_MASK] = it.bytes }

                for ((id, mask, data) in msd.data) setManufacturerData(id, mask, data)
                serviceData.uuid?.let { uuid -> setServiceUuid(uuid, serviceData.mask) }
            }.apply(build)
        }.build()
