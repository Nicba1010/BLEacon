package de.troido.bleacon.data.tuples

import de.troido.bleacon.data.BleDeserializer
import de.troido.bleacon.data.then
import de.troido.ekstend.functional.tuples.Tuple4
import de.troido.ekstend.functional.tuples.flat

class Tuple4Deserializer<out A : Any, out B : Any, out C : Any, out D : Any>(
        private val deserializerA: BleDeserializer<A>,
        private val deserializerB: BleDeserializer<B>,
        private val deserializerC: BleDeserializer<C>,
        private val deserializerD: BleDeserializer<D>
) : BleDeserializer<Tuple4<A, B, C, D>> {

    override val length: Int = deserializerA.length + deserializerB.length +
            deserializerC.length + deserializerD.length

    override fun deserialize(data: ByteArray): Tuple4<A, B, C, D>? =
            deserializerA.then(deserializerB).then(deserializerC).then(deserializerD)
                    .deserialize(data)?.flat()
}
