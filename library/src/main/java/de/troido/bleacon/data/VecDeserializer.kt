package de.troido.bleacon.data

import de.troido.bleacon.util.sequence

/**
 * Fixed size list deserializer.
 *
 * @param[size] size of the vector.
 * @param[deserializer] deserializer for individual vector elements.
 */
class VecDeserializer<out X : Primitive>(
        private val size: Int,
        private val deserializer: BleDeserializer<X>
) : BleDeserializer<List<X>> {

    override val length = deserializer.length * size

    override fun deserialize(data: ByteArray): List<X>? =
            (0 until size)
                    .map {
                        deserializer.deserialize(data.copyOfRange(
                                it * deserializer.length,
                                (it + 1) * deserializer.length
                        ))
                    }
                    .sequence()
}
