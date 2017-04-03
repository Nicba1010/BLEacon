package de.troido.bleacon.data

class Mapping<A, out B>(
        private val deserializer: BleDeserializer<A>,
        private val f: (A) -> B?
) : BleDeserializer<B> {

    override val length = deserializer.length

    override fun deserialize(data: ByteArray): B? = deserializer.deserialize(data)?.let(f)
}
