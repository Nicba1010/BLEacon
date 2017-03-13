package de.troido.bleacon.util

internal val NORDIC_ID = toManufacturerId(byteArrayOf(0x59, 0x00))

private fun toManufacturerId(data: ByteArray): Int = (data[1].toUInt() shl 8) + data[0].toUInt()
