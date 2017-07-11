package de.troido.bleacon.scanner

import android.bluetooth.BluetoothDevice

data class BeaconMetaData(val device: BluetoothDevice,
                          val rssi: Int,
                          val txPowerLevel: Int)
