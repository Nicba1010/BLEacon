package de.troido.bleacon.data.tuples

import de.troido.bleacon.data.BleDeserializer
import de.troido.bleacon.data.then
import de.troido.ekstend.functional.tuples.flat

class TripleDeserializer<out A : Any, out B : Any, out C : Any>(
        private val deserializerA: BleDeserializer<A>,
        private val deserializerB: BleDeserializer<B>,
        private val deserializerC: BleDeserializer<C>
) : BleDeserializer<Triple<A, B, C>> {

    override val length: Int = deserializerA.length + deserializerB.length + deserializerC.length

    override fun deserialize(data: ByteArray): Triple<A, B, C>? =
            deserializerA.then(deserializerB).then(deserializerC)
                    .deserialize(data)?.flat()
}
