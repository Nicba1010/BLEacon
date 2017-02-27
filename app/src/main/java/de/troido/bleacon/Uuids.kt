package de.troido.bleacon

import java.util.*

private const val HEX_CHARS = "0123456789abcdefABCDEF"
private const val BASE_UUID = "-0000-1000-8000-00805F9B34FB"

abstract class UuidLike(bytes: ByteArray) {
    protected val safeBytes = bytes.copyOf()

    val bytes: ByteArray
        get() = safeBytes.copyOf()

    abstract fun toUuid(): UUID

    override fun toString(): String = safeBytes.toHex()
}

class Uuid32(bytes: ByteArray) : UuidLike(bytes) {
    companion object {
        fun fromString(s: String): Uuid32 = Uuid32(hexStringToByteArray(s))
    }

    override fun toUuid(): UUID = UUID.fromString(safeBytes.toHex() + BASE_UUID)
}

class Uuid16(bytes: ByteArray) : UuidLike(bytes) {
    companion object {
        fun fromString(s: String): Uuid16 = Uuid16(hexStringToByteArray(s))
    }

    override fun toUuid(): UUID = UUID.fromString(safeBytes.toHex() + "0000" + BASE_UUID)
}

fun UUID.toBytes(): ByteArray = hexStringToByteArray(toString().replace("-", ""))
