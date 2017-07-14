package de.troido.bleacon.config

import android.bluetooth.le.AdvertiseSettings

class BleAdSettings
@JvmOverloads constructor(
        advertiseMode: Int? = null,
        isConnectable: Boolean? = null,
        txPowerLevel: Int? = null,
        timeout: Int? = null
) {
    internal val settings = AdvertiseSettings.Builder()
            .apply {
                advertiseMode?.let(this::setAdvertiseMode)
                isConnectable?.let(this::setConnectable)
                txPowerLevel?.let(this::setTxPowerLevel)
                timeout?.let(this::setTxPowerLevel)
            }
            .build()
}
