package de.troido.bleacon

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.SystemClock

private const val RESTART_DELAY: Long = 100

abstract class BleaconService(uuid32: Uuid32) : Service() {
    class BleaconBinder(val service: BleaconService) : Binder()

    abstract fun onFound(scanner: ContinuousScanner, type: Byte, data: ByteArray): Unit

    private val scanner = ContinuousScanner(uuid32) { s, t, d -> onFound(s, t, d) }

    override fun onBind(intent: Intent?): IBinder = BleaconBinder(this)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int =
            Service.START_STICKY

    override fun onCreate() {
        super.onCreate()
        scanner.start()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
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
}
