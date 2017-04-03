package de.troido.bleacon.ble

import de.troido.bleacon.util.hexStringToByteArray
import de.troido.bleacon.util.toHex
import java.util.*

private const val BASE_UUID = "0000-0000-1000-8000-00805F9B34FB"

class Uuid16(bytes: ByteArray) {
    private val safeBytes = bytes.copyOf()

    val bytes: ByteArray
        get() = safeBytes.copyOf()

    companion object {
        fun fromString(s: String): Uuid16 = Uuid16(hexStringToByteArray(s))
    }

    fun toUuid(): UUID = UUID.fromString(safeBytes.toHex() + BASE_UUID)

    override fun toString(): String = safeBytes.toHex()
}
