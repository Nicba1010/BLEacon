package de.troido.bleacon.ble

import android.os.Handler
import de.troido.bleacon.util.postDelayed

abstract class HandledBleActor(protected val handler: Handler = Handler()) : BleActor {
    override fun start(millis: Long) {
        start()
        handler.postDelayed(millis, this::stop)
    }

    override fun pause(millis: Long) {
        stop()
        handler.postDelayed(millis, this::start)
    }
}
