package de.troido.bleacon.data

class NullDeserializer<T> : BleDeserializer<T> {
    override val length: Int = 0
    override fun deserialize(data: ByteArray): T? = null
}
