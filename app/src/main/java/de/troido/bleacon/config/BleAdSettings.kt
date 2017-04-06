package de.troido.bleacon.config

import android.bluetooth.le.AdvertiseSettings
import de.troido.bleacon.util.Building

/**
 * DSL for [AdvertiseSettings].
 */
class BleAdSettings(internal val settings: AdvertiseSettings) {

    constructor(builder: BleAdSettings.Builder) :
            this(builder.settings.build())

    constructor(build: BleAdSettings.Builder.() -> Unit) :
            this(BleAdSettings.Builder().apply(build))

    class Builder {
        internal val settings = AdvertiseSettings.Builder()

        /** See [AdvertiseSettings.setAdvertiseMode] */
        var advertiseMode: Int? by Building(settings::setAdvertiseMode)

        /** See [AdvertiseSettings.setConnectable] */
        var isConnectable: Boolean? by Building(settings::setConnectable)

        /** See [AdvertiseSettings.setTxPowerLevel] */
        var txPowerLevel: Int? by Building(settings::setTxPowerLevel)

        /** See [AdvertiseSettings.setTimeout] */
        var timeout: Int? by Building(settings::setTimeout)
    }
}
