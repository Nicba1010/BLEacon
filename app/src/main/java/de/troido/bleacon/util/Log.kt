package de.troido.bleacon.util

import android.util.Log

internal inline fun <T : Any> T.log(msg: Any?) {
    Log.d(this::class.java.simpleName, msg.toString())
}

internal inline fun logger(tag: String): (Any?) -> Unit = {
    Log.d(tag, it.toString())
}
