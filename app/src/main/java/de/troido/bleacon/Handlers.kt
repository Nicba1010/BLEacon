package de.troido.bleacon

import android.os.Handler

fun Handler.postDelayed(delayMillis: Long, r: () -> Unit) = postDelayed(r, delayMillis)
