package de.troido.bleacon.config.scan

import android.bluetooth.le.ScanSettings
import android.os.Build
import android.support.annotation.RequiresApi


@JvmOverloads
fun scanSettings(scanMode: Int = ScanSettings.SCAN_MODE_LOW_POWER,
                 reportDelay: Long = 0): ScanSettings =
        ScanSettings.Builder()
                .apply {
                    setScanMode(scanMode)
                    setReportDelay(reportDelay)
                }
                .build()

@RequiresApi(Build.VERSION_CODES.M)
fun scanSettings(scanMode: Int = ScanSettings.SCAN_MODE_LOW_POWER,
                 reportDelay: Long = 0,
                 callbackType: Int = ScanSettings.CALLBACK_TYPE_ALL_MATCHES,
                 matchMode: Int = ScanSettings.MATCH_MODE_AGGRESSIVE,
                 numOfMatches: Int = ScanSettings.MATCH_NUM_MAX_ADVERTISEMENT): ScanSettings =
        ScanSettings.Builder()
                .apply {
                    setScanMode(scanMode)
                    setReportDelay(reportDelay)
                    setCallbackType(callbackType)
                    setMatchMode(matchMode)
                    setNumOfMatches(numOfMatches)
                }
                .build()
