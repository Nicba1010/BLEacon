package de.troido.bleacon.util

import android.app.AlarmManager
import android.content.Context

/** Returns [Context.ALARM_SERVICE] system service. */
internal val Context.alarmService: AlarmManager
    get() = getSystemService(Context.ALARM_SERVICE) as AlarmManager
