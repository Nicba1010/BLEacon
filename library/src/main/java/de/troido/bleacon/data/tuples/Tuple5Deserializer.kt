package de.troido.bleacon.data.tuples

import de.troido.bleacon.data.BleDeserializer
import de.troido.bleacon.data.then
import de.troido.ekstend.functional.tuples.Tuple5
import de.troido.ekstend.functional.tuples.flat

class Tuple5Deserializer<out A : Any, out B : Any, out C : Any, out D : Any, out E : Any>(
        private val deserializerA: BleDeserializer<A>,
        private val deserializerB: BleDeserializer<B>,
        private val deserializerC: BleDeserializer<C>,
        private val deserializerD: BleDeserializer<D>,
        private val deserializerE: BleDeserializer<E>
) : BleDeserializer<Tuple5<A, B, C, D, E>> {

    override val length: Int = deserializerA.length + deserializerB.length +
            deserializerC.length + deserializerD.length + deserializerE.length

    override fun deserialize(data: ByteArray): Tuple5<A, B, C, D, E>? =
            deserializerA.then(deserializerB).then(deserializerC).then(deserializerD)
                    .then(deserializerE)
                    .deserialize(data)?.flat()
}
