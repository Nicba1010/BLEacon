package de.troido.bleacon.ble

/**
 * Basic interface for controlling BLE actions.
 */
interface BleActor {

    /** Run the actor indefinitely. */
    fun start(): Unit

    /** Stop the actor. */
    fun stop(): Unit

    /** Run the actor for [millis] milliseconds. */
    fun start(millis: Long): Unit

    /** Pause the actor for [millis] milliseconds. */
    fun pause(millis: Long): Unit
}
