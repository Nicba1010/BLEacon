package de.troido.bleacon.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.SystemClock
import de.troido.bleacon.ble.BleActor
import de.troido.bleacon.util.alarmService

private const val RESTART_DELAY: Long = 100

abstract class BleService<out T>(
        private val autoStart: Boolean = true,
        private val restartOnRemove: Boolean = true
) : Service() {

    inner class BleBinder() : Binder(), BleActor {
        override fun start() = actors.forEach(BleActor::start)
        override fun stop() = actors.forEach(BleActor::stop)
        override fun start(millis: Long) = actors.forEach { it.start(millis) }
        override fun pause(millis: Long) = actors.forEach { it.pause(millis) }
    }

    protected abstract val actors: List<BleActor>

    override fun onBind(intent: Intent?): IBinder = BleBinder()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int =
            START_STICKY

    override fun onCreate() {
        super.onCreate()
        if (autoStart) {
            actors.forEach(BleActor::start)
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)

        actors.forEach(BleActor::stop)
        if (restartOnRemove) {
            applicationContext.alarmService.set(
                    AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime() + RESTART_DELAY,
                    PendingIntent.getService(
                            applicationContext,
                            1,
                            Intent(applicationContext, javaClass),
                            PendingIntent.FLAG_ONE_SHOT
                    )
            )
        }
    }

    override fun onDestroy() {
        actors.forEach(BleActor::stop)
        super.onDestroy()
    }
}
