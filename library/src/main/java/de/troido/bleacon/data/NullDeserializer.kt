package de.troido.bleacon.data

class NullDeserializer(override val length: Int = 0): BleDeserializer<Any> {
    override fun deserialize(data: ByteArray): Any? = null
}
