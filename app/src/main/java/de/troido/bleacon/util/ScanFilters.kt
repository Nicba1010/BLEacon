package de.troido.bleacon.util

import android.bluetooth.le.ScanFilter

private val EMPTY = byteArrayOf()

internal fun filterByManufacturerData(id: Int): ScanFilter =
        filterByManufacturerData(id, EMPTY)

internal fun filterByManufacturerData(id: Int, data: ByteArray): ScanFilter =
        ScanFilter.Builder()
                .setManufacturerData(id, data)
                .build()
