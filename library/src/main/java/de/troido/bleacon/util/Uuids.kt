package de.troido.bleacon.util

import java.util.UUID

private const val HEX_CHARS = "0123456789abcdefABCDEF"

internal val UUID.bytes: ByteArray
    get() = hexStringToByteArray(toString().replace("-", ""))
