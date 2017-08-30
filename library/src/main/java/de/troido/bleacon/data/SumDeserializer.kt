package de.troido.bleacon.data

abstract class SumDeserializer<out T> : BleDeserializer<T> {

    override val length: Int = BleDeserializer.ALL

    abstract val deserializers: Map<Byte, BleDeserializer<T>>

    override fun deserialize(data: ByteArray): T? {
        if (data.isEmpty()) return null
        return deserializers[data[0]]?.takeIf { it.matchesLength(data) }?.deserialize(data)
    }
}
