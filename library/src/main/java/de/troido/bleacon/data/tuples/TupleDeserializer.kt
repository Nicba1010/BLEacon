package de.troido.bleacon.data.tuples

import de.troido.bleacon.data.BleDeserializer
import de.troido.ekstend.functional.tuples.Tuple4
import de.troido.ekstend.functional.tuples.Tuple5
import de.troido.ekstend.functional.tuples.Tuple6
import de.troido.ekstend.functional.tuples.Tuple7
import de.troido.ekstend.functional.tuples.Tuple8
import de.troido.ekstend.functional.tuples.Tuple9

fun <A, B> tupleDeserializer(
        deserializerA: BleDeserializer<A>, deserializerB: BleDeserializer<B>
): BleDeserializer<Pair<A, B>> =
        PairDeserializer(deserializerA, deserializerB)

fun <A, B, C> tupleDeserializer(
        deserializerA: BleDeserializer<A>, deserializerB: BleDeserializer<B>,
        deserializerC: BleDeserializer<C>
): BleDeserializer<Triple<A, B, C>> =
        TripleDeserializer(deserializerA, deserializerB, deserializerC)

fun <A, B, C, D> tupleDeserializer(
        deserializerA: BleDeserializer<A>, deserializerB: BleDeserializer<B>,
        deserializerC: BleDeserializer<C>, deserializerD: BleDeserializer<D>
): BleDeserializer<Tuple4<A, B, C, D>> =
        Tuple4Deserializer(deserializerA, deserializerB, deserializerC, deserializerD)

fun <A, B, C, D, E> tupleDeserializer(
        deserializerA: BleDeserializer<A>, deserializerB: BleDeserializer<B>,
        deserializerC: BleDeserializer<C>, deserializerD: BleDeserializer<D>,
        deserializerE: BleDeserializer<E>
): BleDeserializer<Tuple5<A, B, C, D, E>> =
        Tuple5Deserializer(deserializerA, deserializerB, deserializerC, deserializerD,
                           deserializerE)

fun <A, B, C, D, E, F> tupleDeserializer(
        deserializerA: BleDeserializer<A>, deserializerB: BleDeserializer<B>,
        deserializerC: BleDeserializer<C>, deserializerD: BleDeserializer<D>,
        deserializerE: BleDeserializer<E>, deserializerF: BleDeserializer<F>
): BleDeserializer<Tuple6<A, B, C, D, E, F>> =
        Tuple6Deserializer(deserializerA, deserializerB, deserializerC, deserializerD,
                           deserializerE, deserializerF)

fun <A, B, C, D, E, F, G> tupleDeserializer(
        deserializerA: BleDeserializer<A>, deserializerB: BleDeserializer<B>,
        deserializerC: BleDeserializer<C>, deserializerD: BleDeserializer<D>,
        deserializerE: BleDeserializer<E>, deserializerF: BleDeserializer<F>,
        deserializerG: BleDeserializer<G>
): BleDeserializer<Tuple7<A, B, C, D, E, F, G>> =
        Tuple7Deserializer(deserializerA, deserializerB, deserializerC, deserializerD,
                           deserializerE, deserializerF, deserializerG)

fun <A, B, C, D, E, F, G, H> tupleDeserializer(
        deserializerA: BleDeserializer<A>, deserializerB: BleDeserializer<B>,
        deserializerC: BleDeserializer<C>, deserializerD: BleDeserializer<D>,
        deserializerE: BleDeserializer<E>, deserializerF: BleDeserializer<F>,
        deserializerG: BleDeserializer<G>, deserializerH: BleDeserializer<H>
): BleDeserializer<Tuple8<A, B, C, D, E, F, G, H>> =
        Tuple8Deserializer(deserializerA, deserializerB, deserializerC, deserializerD,
                           deserializerE, deserializerF, deserializerG, deserializerH)

fun <A, B, C, D, E, F, G, H, I> tupleDeserializer(
        deserializerA: BleDeserializer<A>, deserializerB: BleDeserializer<B>,
        deserializerC: BleDeserializer<C>, deserializerD: BleDeserializer<D>,
        deserializerE: BleDeserializer<E>, deserializerF: BleDeserializer<F>,
        deserializerG: BleDeserializer<G>, deserializerH: BleDeserializer<H>,
        deserializerI: BleDeserializer<I>
): BleDeserializer<Tuple9<A, B, C, D, E, F, G, H, I>> =
        Tuple9Deserializer(deserializerA, deserializerB, deserializerC, deserializerD,
                           deserializerE, deserializerF, deserializerG, deserializerH,
                           deserializerI)
