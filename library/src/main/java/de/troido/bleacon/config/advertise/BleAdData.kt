package de.troido.bleacon.config.advertise

import android.bluetooth.le.AdvertiseData
import de.troido.bleacon.ble.NORDIC_ID
import de.troido.ekstend.uuid.Uuid16
import de.troido.ekstend.uuid.bytes
import java.util.UUID

@JvmOverloads
fun bleAdData(uuid16: Uuid16? = null,
              uuid128: UUID? = null,
              includeTxPowerLevel: Boolean = false,
              includeDeviceName: Boolean = false,
              build: AdDataBuilder.() -> Unit = {}): AdvertiseData =

        AdvertiseData.Builder()
                .apply {
                    setIncludeTxPowerLevel(includeTxPowerLevel)
                    setIncludeDeviceName(includeDeviceName)

                    AdDataBuilder(this).apply {
                        uuid16?.let { msd[NORDIC_ID] = it.bytes }
                        uuid128?.let { msd[NORDIC_ID] = it.bytes }
                    }.apply(build)
                }
                .build()
