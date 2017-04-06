package de.troido.bleacon.config

import android.bluetooth.le.ScanSettings
import de.troido.bleacon.util.Building

/**
 * DSL for [ScanSettings].
 */
class BleScanSettings(internal val settings: ScanSettings) {

    constructor(builder: BleScanSettings.Builder) :
            this(builder.settings.build())

    constructor(build: BleScanSettings.Builder.() -> Unit) :
            this(BleScanSettings.Builder().apply(build))

    class Builder {
        internal val settings = ScanSettings.Builder()

        /** See [ScanSettings.Builder.setScanMode]. */
        var scanMode: Int? by Building(settings::setScanMode)

        /** See [ScanSettings.Builder.setCallbackType]. */
        var callbackType: Int? by Building(settings::setCallbackType)

        /** See [ScanSettings.Builder.setMatchMode]. */
        var matchMode: Int? by Building(settings::setMatchMode)

        /** See [::ScanSettings.Builder.setNumOfMatches]. */
        var numOfMatches: Int? by Building(settings::setNumOfMatches)

        /** See [ScanSettings.Builder.setReportDelay]. */
        var reportDelay: Long? by Building(settings::setReportDelay)
    }
}
