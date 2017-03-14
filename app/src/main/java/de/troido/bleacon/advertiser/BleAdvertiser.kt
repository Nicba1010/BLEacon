package de.troido.bleacon.advertiser

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.os.Handler
import de.troido.bleacon.ble.HandledBleActor
import de.troido.bleacon.scanner.Uuid16
import de.troido.bleacon.util.NORDIC_ID
import de.troido.bleacon.util.bytes
import java.util.*

private val header = byteArrayOf(-1, -1)

class BleAdvertiser(
        uuid16: Uuid16? = null,
        uuid128: UUID? = null,
        advertiseMode: Int? = null,
        txPowerLevel: Int? = null,
        handler: Handler = Handler()
) : HandledBleActor(handler) {

    private val advertiser = obtainAdvertiser()

    private val data = AdvertiseData.Builder()
            .setIncludeDeviceName(false)
            .setIncludeTxPowerLevel(false)
            .apply {
                uuid16?.bytes?.let { addManufacturerData(NORDIC_ID, header + it) }
                uuid128?.bytes?.let { addManufacturerData(NORDIC_ID, header + it) }
            }
            .build()

    private val callback = object : AdvertiseCallback() {}

    private val settings = AdvertiseSettings.Builder()
            .setConnectable(false)
            .apply {
                advertiseMode?.let(this::setAdvertiseMode)
                txPowerLevel?.let(this::setTxPowerLevel)
            }
            .build()

    override fun start() {
        handler.post { advertiser.startAdvertising(settings, data, data, callback) }
    }

    override fun stop() {
        handler.post { advertiser.stopAdvertising(callback) }
    }
}

private fun obtainAdvertiser(): BluetoothLeAdvertiser {
    val adapter = BluetoothAdapter.getDefaultAdapter()
    if (!adapter.isEnabled) {
        adapter.enable()
    }
    while (true) {
        adapter.bluetoothLeAdvertiser?.let { return it }
    }
}
