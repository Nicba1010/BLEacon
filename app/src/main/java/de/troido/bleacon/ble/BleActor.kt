package de.troido.bleacon.ble

interface BleActor {
    fun start(): Unit
    fun stop(): Unit
    fun start(millis: Long): Unit
    fun pause(millis: Long): Unit
}
