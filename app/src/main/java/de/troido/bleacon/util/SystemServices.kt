package de.troido.bleacon.util

import android.app.AlarmManager
import android.content.Context

internal val Context.alarmService: AlarmManager
    get() = getSystemService(Context.ALARM_SERVICE) as AlarmManager
