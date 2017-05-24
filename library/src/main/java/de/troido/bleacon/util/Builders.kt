package de.troido.bleacon.util

import kotlin.reflect.KProperty

/**
 * Delegate for idiomatic builder wrappers.
 */
internal class Building<T : Any>(private val setter: (T) -> Any?) {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? = null

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        value?.let(setter)
    }
}
