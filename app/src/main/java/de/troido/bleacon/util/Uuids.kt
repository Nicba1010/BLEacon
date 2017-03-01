package de.troido.bleacon.util

import java.util.*

private const val HEX_CHARS = "0123456789abcdefABCDEF"

internal fun UUID.toBytes(): ByteArray = hexStringToByteArray(toString().replace("-", ""))
