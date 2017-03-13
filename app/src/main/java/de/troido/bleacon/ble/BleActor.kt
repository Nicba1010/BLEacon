package de.troido.bleacon.ble

import android.os.Handler
import de.troido.bleacon.util.postDelayed

abstract class BleActor {
    private val handler = Handler()

    abstract fun start(): Unit
    abstract fun stop(): Unit

    fun start(millis: Long) {
        start()
        handler.postDelayed(millis, this::stop)
    }

    fun pause(millis: Long) {
        stop()
        handler.postDelayed(millis, this::start)
    }
}
