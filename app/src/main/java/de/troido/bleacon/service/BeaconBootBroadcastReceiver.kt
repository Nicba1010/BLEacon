package de.troido.bleacon.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * [BroadcastReceiver] which starts the specified [service] on boot (assuming it's correctly
 * registered in the manifest).
 */
abstract class ServiceBootBroadcastReceiver<S : Service> : BroadcastReceiver() {
    abstract val service: Class<S>

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.startService(Intent(context, service))
    }
}
