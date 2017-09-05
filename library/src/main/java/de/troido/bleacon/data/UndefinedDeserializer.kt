package de.troido.bleacon.data

class UndefinedDeserializer<out T>(override val length: Int = 0) : BleDeserializer<T> {
    override fun deserialize(data: ByteArray): T? = null
}

fun <T> BleDeserializer<T>.ignored(): BleDeserializer<T> =
        UndefinedDeserializer(length)
