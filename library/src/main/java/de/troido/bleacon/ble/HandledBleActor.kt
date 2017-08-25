package de.troido.bleacon.ble

import android.os.Handler
import de.troido.ekstend.android.threads.postDelayed

/**
 * [BleActor] with [BleActor.start]`(Long)` and [BleActor.pause] implemented through a [Handler].
 *
 * It's advised to implement [BleActor.start] and [BleActor.stop] through this [handler] too.
 */
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
