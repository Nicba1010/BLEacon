package de.troido.bleacon.data.tuples

import de.troido.bleacon.data.BleDeserializer
import de.troido.bleacon.data.then

class PairDeserializer<out A : Any, out B : Any>(
        private val deserializerA: BleDeserializer<A>,
        private val deserializerB: BleDeserializer<B>
) : BleDeserializer<Pair<A, B>> {

    override val length: Int = deserializerA.length + deserializerB.length

    override fun deserialize(data: ByteArray): Pair<A, B>? =
            deserializerA.then(deserializerB)
                    .deserialize(data)
}
