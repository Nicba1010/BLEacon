package de.troido.bleacon.data

import de.troido.ekstend.functional.sequence

/**
 * Fixed size list deserializer.
 *
 * @param[size] size of the vector.
 * @param[deserializer] deserializer for individual vector elements.
 */
class VecDeserializer<out X : Any>(
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
