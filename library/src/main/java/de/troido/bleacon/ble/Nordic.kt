package de.troido.bleacon.ble

import de.troido.ekstend.numeric.toUShort

/** Nordic Semiconductors manufacturer ID. */
val NORDIC_ID = toManufacturerId(byteArrayOf(0x59, 0x00))

private fun toManufacturerId(data: ByteArray): Int =
        (data[1].toUShort() shl 8) + data[0].toUShort()
