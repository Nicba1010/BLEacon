package de.troido.bleacon.data

import de.troido.bleacon.util.joinNull

class VecDeserializer<out X : Primitive>(
        size: Int,
        private val deserializer: BleDeserializer<X>
) : BleDeserializer<List<X>> {

    override val length = deserializer.length * size

    override fun deserialize(data: ByteArray): List<X>? = (0 until length)
            .map { deserializer.deserialize(data.copyOfRange(it, it + deserializer.length)) }
            .joinNull()
}
