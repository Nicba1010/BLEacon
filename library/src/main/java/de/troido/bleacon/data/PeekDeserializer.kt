package de.troido.bleacon.data

class PeekDeserializer(private val f: (ByteArray) -> Unit) : BleDeserializer<ByteArray> {
    override val length: Int = BleDeserializer.ALL

    override fun deserialize(data: ByteArray): ByteArray? {
        f(data.copyOf())
        return data
    }
}
