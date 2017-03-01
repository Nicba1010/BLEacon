package de.troido.bleacon.util

import android.bluetooth.le.ScanFilter

private val EMPTY = byteArrayOf()

internal fun filterByManufacturerData(id: Int, data: ByteArray = EMPTY): ScanFilter =
        ScanFilter.Builder()
                .setManufacturerData(id, data)
                .build()
