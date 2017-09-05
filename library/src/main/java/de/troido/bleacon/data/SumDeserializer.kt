package de.troido.bleacon.data

import de.troido.ekstend.collections.copyOfRange
import kotlin.experimental.and

class SumDeserializer<out T>(
        private val removePrefix: Boolean = true,
        private val prefixMask: Byte = 0xFF.toByte(),
        private val deserializers: Map<Byte, BleDeserializer<T>>
) : BleDeserializer<T> {

    override val length: Int = BleDeserializer.ALL

    override fun deserialize(data: ByteArray): T? = when {
        data.isEmpty() -> null
        else           -> deserializers[data[0] and prefixMask]
                ?.takeIf { it.matchesLength(data) }
                ?.deserialize(if (removePrefix) data.copyOfRange(1) else data)
    }
}
