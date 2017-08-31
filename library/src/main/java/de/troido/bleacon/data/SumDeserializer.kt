package de.troido.bleacon.data

class SumDeserializer<out T>(private val deserializers: Map<Byte, BleDeserializer<T>>)
    : BleDeserializer<T> {

    override val length: Int = BleDeserializer.ALL

    override fun deserialize(data: ByteArray): T? {
        if (data.isEmpty()) return null
        return deserializers[data[0]]?.takeIf { it.matchesLength(data) }?.deserialize(data)
    }
}
