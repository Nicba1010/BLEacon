package de.troido.bleacon.data

import de.troido.bleacon.util.joinNull

class VecDeserializer<out X : Primitive>(
        private val size: Int,
        private val deserializer: BleDeserializer<X>
) : BleDeserializer<List<X>> {

    override val length = deserializer.length * size

    override fun deserialize(data: ByteArray): List<X>? = (0 until size)
            .map {
                deserializer.deserialize(data.copyOfRange(
                        it * deserializer.length,
                        (it + 1) * deserializer.length
                ))
            }
            .joinNull()
}
