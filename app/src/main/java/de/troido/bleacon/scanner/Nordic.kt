package de.troido.bleacon.scanner

import de.troido.bleacon.util.toUInt

internal val NORDIC_ID = toManufacturerId(byteArrayOf(0x59, 0x00))

internal fun toManufacturerId(data: ByteArray): Int = (data[1].toUInt() shl 8) + data[0].toUInt()
