package de.troido.bleacon.util

import android.os.Handler

/** [Handler.postDelayed] with flipped parameters for more idiomatic Kotlin code. */
internal inline fun Handler.postDelayed(delayMillis: Long, noinline r: () -> Unit) =
        postDelayed(r, delayMillis)
