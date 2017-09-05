package de.troido.bleacon.data.tuples

import de.troido.bleacon.data.BleDeserializer
import de.troido.bleacon.data.then
import de.troido.ekstend.functional.tuples.Tuple9
import de.troido.ekstend.functional.tuples.flat

class Tuple9Deserializer<out A, out B, out C, out D, out E, out F, out G, out H, out I>(
        private val deserializerA: BleDeserializer<A>,
        private val deserializerB: BleDeserializer<B>,
        private val deserializerC: BleDeserializer<C>,
        private val deserializerD: BleDeserializer<D>,
        private val deserializerE: BleDeserializer<E>,
        private val deserializerF: BleDeserializer<F>,
        private val deserializerG: BleDeserializer<G>,
        private val deserializerH: BleDeserializer<H>,
        private val deserializerI: BleDeserializer<I>
) : BleDeserializer<Tuple9<A, B, C, D, E, F, G, H, I>> {

    override val length: Int = deserializerA.length + deserializerB.length +
            deserializerC.length + deserializerD.length + deserializerE.length +
            deserializerF.length + deserializerG.length + deserializerH.length +
            deserializerI.length

    override fun deserialize(data: ByteArray): Tuple9<A, B, C, D, E, F, G, H, I>? =
            deserializerA.then(deserializerB).then(deserializerC).then(deserializerD)
                    .then(deserializerE).then(deserializerF).then(deserializerG)
                    .then(deserializerH).then(deserializerI)
                    .deserialize(data)?.flat()
}
