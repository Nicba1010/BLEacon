package de.troido.bleacon.data.tuples

import de.troido.bleacon.data.BleDeserializer
import de.troido.bleacon.data.then
import de.troido.ekstend.functional.tuples.Tuple6
import de.troido.ekstend.functional.tuples.flat

class Tuple6Deserializer<out A : Any, out B : Any, out C : Any, out D : Any, out E : Any,
        out F : Any>(

        private val deserializerA: BleDeserializer<A>,
        private val deserializerB: BleDeserializer<B>,
        private val deserializerC: BleDeserializer<C>,
        private val deserializerD: BleDeserializer<D>,
        private val deserializerE: BleDeserializer<E>,
        private val deserializerF: BleDeserializer<F>
) : BleDeserializer<Tuple6<A, B, C, D, E, F>> {

    override val length: Int = deserializerA.length + deserializerB.length +
            deserializerC.length + deserializerD.length + deserializerE.length +
            deserializerF.length

    override fun deserialize(data: ByteArray): Tuple6<A, B, C, D, E, F>? =
            deserializerA.then(deserializerB).then(deserializerC).then(deserializerD)
                    .then(deserializerE).then(deserializerF)
                    .deserialize(data)?.flat()
}
