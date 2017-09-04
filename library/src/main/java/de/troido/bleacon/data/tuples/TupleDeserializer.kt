package de.troido.bleacon.data.tuples

import de.troido.bleacon.data.BleDeserializer
import de.troido.ekstend.functional.Tuple4
import de.troido.ekstend.functional.Tuple5

fun <A : Any, B : Any> tupleDeserializer(
        deserializerA: BleDeserializer<A>,
        deserializerB: BleDeserializer<B>
): BleDeserializer<Pair<A, B>> =
        PairDeserializer(deserializerA, deserializerB)

fun <A : Any, B : Any, C : Any> tupleDeserializer(
        deserializerA: BleDeserializer<A>,
        deserializerB: BleDeserializer<B>,
        deserializerC: BleDeserializer<C>
): BleDeserializer<Triple<A, B, C>> =
        TripleDeserializer(deserializerA, deserializerB, deserializerC)

fun <A : Any, B : Any, C : Any, D : Any> tupleDeserializer(
        deserializerA: BleDeserializer<A>,
        deserializerB: BleDeserializer<B>,
        deserializerC: BleDeserializer<C>,
        deserializerD: BleDeserializer<D>
): BleDeserializer<Tuple4<A, B, C, D>> =
        Tuple4Deserializer(deserializerA, deserializerB, deserializerC, deserializerD)

fun <A : Any, B : Any, C : Any, D : Any, E : Any> tupleDeserializer(
        deserializerA: BleDeserializer<A>,
        deserializerB: BleDeserializer<B>,
        deserializerC: BleDeserializer<C>,
        deserializerD: BleDeserializer<D>,
        deserializerE: BleDeserializer<E>
): BleDeserializer<Tuple5<A, B, C, D, E>> =
        Tuple5Deserializer(deserializerA, deserializerB, deserializerC, deserializerD,
                           deserializerE)
