package de.troido.bleacon.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.SystemClock
import de.troido.bleacon.scanner.BleaconScanner

private const val RESTART_DELAY: Long = 100

abstract class BleaconService : Service() {
    class BleaconBinder(val service: BleaconService) : Binder()

    protected abstract val scanner: BleaconScanner

    override fun onBind(intent: Intent?): IBinder = BleaconBinder(this)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int =
            START_STICKY

    override fun onCreate() {
        super.onCreate()
        scanner.start()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        scanner.stop()
        (applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager).set(
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

    override fun onDestroy() {
        scanner.stop()
        super.onDestroy()
    }
}
