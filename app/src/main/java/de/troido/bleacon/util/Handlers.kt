package de.troido.bleacon.util

import android.os.Handler

internal fun Handler.postDelayed(delayMillis: Long, r: () -> Unit) = postDelayed(r, delayMillis)
