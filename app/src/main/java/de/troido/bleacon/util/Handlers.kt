package de.troido.bleacon.util

import android.os.Handler

internal inline fun Handler.postDelayed(delayMillis: Long, noinline r: () -> Unit) =
        postDelayed(r, delayMillis)
