package de.troido.bleacon.config.advertise

import android.bluetooth.le.AdvertiseSettings

@JvmOverloads
fun adSettings(advertiseMode: Int = AdvertiseSettings.ADVERTISE_MODE_LOW_POWER,
               isConnectable: Boolean = true,
               txPowerLevel: Int = AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM,
               timeout: Int = 0): AdvertiseSettings =

        AdvertiseSettings.Builder()
                .apply {
                    setAdvertiseMode(advertiseMode)
                    setConnectable(isConnectable)
                    setTxPowerLevel(txPowerLevel)
                    setTimeout(timeout)
                }
                .build()
