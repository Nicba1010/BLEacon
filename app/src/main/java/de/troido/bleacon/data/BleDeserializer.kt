package de.troido.bleacon.data

interface BleDeserializer<out T> {
    val length: Int
    fun deserialize(data: ByteArray): T?
}

fun <A, B> BleDeserializer<A>.mapping(f: (A) -> B?): BleDeserializer<B> = Mapping(this, f)
