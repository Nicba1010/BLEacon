package de.troido.bleacon.util

import java.util.Queue

@Suppress("NOTHING_TO_INLINE", "SENSELESS_COMPARISON")
internal inline fun <T : Any> Queue<T>.sequence(): Sequence<T> =
        generateSequence(this::poll).takeWhile { it != null }

internal inline fun <T : Any> Queue<T>.forEachPolled(f: (T) -> Unit): Unit =
        sequence().forEach(f)
