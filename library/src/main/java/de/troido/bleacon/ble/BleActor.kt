package de.troido.bleacon.ble

/**
 * Basic interface for controlling BLE actions.
 */
interface BleActor {

    /** Run the actor indefinitely. */
    fun start()

    /** Stop the actor. */
    fun stop()

    /** Run the actor for [millis] milliseconds. */
    fun start(millis: Long)

    /** Pause the actor for [millis] milliseconds. */
    fun pause(millis: Long)
}
