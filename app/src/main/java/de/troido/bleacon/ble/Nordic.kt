package de.troido.bleacon.ble

import de.troido.bleacon.util.toUInt

/** Nordic Semiconductors manufacturer ID. */
val NORDIC_ID = toManufacturerId(byteArrayOf(0x59, 0x00))

private fun toManufacturerId(data: ByteArray): Int = (data[1].toUInt() shl 8) + data[0].toUInt()
