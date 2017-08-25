package de.troido.bleacon.data

import de.troido.ekstend.functional.Tuple4
import de.troido.ekstend.functional.flat

class PairDeserializer<out A : Any, out B : Any>(
        private val deserializerA: BleDeserializer<A>,
        private val deserializerB: BleDeserializer<B>
) : BleDeserializer<Pair<A, B>> {

    override val length: Int = deserializerA.length + deserializerB.length

    override fun deserialize(data: ByteArray): Pair<A, B>? =
            deserializerA.then(deserializerB).deserialize(data)
}

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

class Tuple4Deserializer<out A : Any, out B : Any, out C : Any, out D : Any>(
        private val deserializerA: BleDeserializer<A>,
        private val deserializerB: BleDeserializer<B>,
        private val deserializerC: BleDeserializer<C>,
        private val deserializerD: BleDeserializer<D>
) : BleDeserializer<Tuple4<A, B, C, D>> {

    private val deserializers = listOf(deserializerA, deserializerB, deserializerC, deserializerD)

    override val length: Int = deserializerA.length + deserializerB.length +
            deserializerC.length + deserializerD.length

    override fun deserialize(data: ByteArray): Tuple4<A, B, C, D>? =
            deserializerA.then(deserializerB).then(deserializerC).then(deserializerD)
                    .deserialize(data)?.flat()
}
