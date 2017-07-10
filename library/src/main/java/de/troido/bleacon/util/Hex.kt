package de.troido.bleacon.util

private const val HEX_CHARS = "0123456789abcdef"

internal fun hexStringToByteArray(s: String): ByteArray = s.toLowerCase().let {
    val result = ByteArray(it.length / 2)
    for (i in 0 until it.length step 2) {
        result[i.shr(1)] = HEX_CHARS.indexOf(it[i]).shl(4).or(HEX_CHARS.indexOf(it[i + 1])).toByte()
    }
    return result
}

internal fun ByteArray.toHex(): String {
    val result = StringBuffer()
    forEach {
        val octet = it.toInt()
        result.append(HEX_CHARS[(octet and 0xF0).ushr(4)])
        result.append(HEX_CHARS[octet and 0x0F])
    }
    return result.toString()
}

/** Returns `this` as an unsigned integer. */
internal inline fun Byte.toUInt(): Int = toInt() and 0xff

/** Returns `this` as an unsigned integer. */
internal inline fun Short.toUInt(): Int = toInt() and 0xffff