package de.troido.bleacon.util

/**
 * Analogous to [kotlin.collections.ArrayAsCollection.fold] but encapsulated in Kotlin's
 * nullable (`T?`) wannabe-monad.
 */
internal inline fun <A, B> Array<A>.foldM(initial: B, f: (B, A) -> B?): B? {
    var accum: B? = initial
    for (x in this) {
        if (accum == null) break
        accum = f(accum, x)
    }
    return accum
}

/**
 * Given a `List<T?>` returns a `List<T>` if no elements are `null`, otherwise returns `null`.
 */
@Suppress("UNCHECKED_CAST")
internal inline fun <T> List<T?>.joinNull(): List<T>? =
        if (any { it == null }) null else this as List<T>
