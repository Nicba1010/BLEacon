package de.troido.bleacon.data

import de.troido.ekstend.collections.copyOfRange

class SumDeserializer<out T>(private val deserializers: Map<Byte, BleDeserializer<T>>)
    : BleDeserializer<T> {

    override val length: Int = BleDeserializer.ALL

    override fun deserialize(data: ByteArray): T? = when {
        data.isEmpty() -> null
        else           -> deserializers[data[0]]
                ?.takeIf { it.matchesLength(data) }
                ?.deserialize(data.copyOfRange(1))
    }
}
