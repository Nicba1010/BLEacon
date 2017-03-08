package de.troido.bleacon.data

interface BleDeserializer<out T> {
    class Mapping<A, out B>(
            private val deserializer: BleDeserializer<A>,
            private val f: (A) -> B?
    ) : BleDeserializer<B> {

        override val length = deserializer.length

        override fun deserialize(data: ByteArray): B? = deserializer.deserialize(data)?.let(f)
    }

    val length: Int
    fun deserialize(data: ByteArray): T?
}
