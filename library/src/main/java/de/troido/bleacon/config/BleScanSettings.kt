package de.troido.bleacon.config

import android.bluetooth.le.ScanSettings


/**
 * DSL for [ScanSettings].
 */
class BleScanSettings @JvmOverloads constructor(
        scanMode: Int? = null,
        callbackType: Int? = null,
        matchMode: Int? = null,
        numOfMatches: Int? = null,
        reportDelay: Long? = null
) {
    internal val settings = ScanSettings.Builder()
            .apply {
                scanMode?.let(this::setScanMode)
                callbackType?.let(this::setCallbackType)
                matchMode?.let(this::setMatchMode)
                numOfMatches?.let(this::setNumOfMatches)
                reportDelay?.let(this::setReportDelay)
            }
            .build()
}
