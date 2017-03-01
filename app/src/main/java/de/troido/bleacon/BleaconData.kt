package de.troido.bleacon

sealed class BleaconData {
    object Confirmation : BleaconData()

    class AnalogInput(val data: Byte) : BleaconData() {
        companion object {
            const val ID: Byte = 0x01
        }
    }
}
