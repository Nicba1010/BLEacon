package de.troido.bleacon

import java.util.*

private const val HEX_CHARS = "0123456789abcdefABCDEF"
private const val BASE_UUID = "-0000-1000-8000-00805F9B34FB"

class Uuid32 private constructor(uuid: String) {
    private val safeBytes = hexStringToByteArray(uuid)

    val bytes: ByteArray
        get() = safeBytes.copyOf()

    companion object {
        fun fromString(s: String): Uuid32? =
                if (s.all { it in HEX_CHARS }) Uuid32(s) else null
    }

    fun toUuid(): UUID = UUID.fromString(safeBytes.toHex() + BASE_UUID)

    override fun toString(): String = safeBytes.toHex()
}
