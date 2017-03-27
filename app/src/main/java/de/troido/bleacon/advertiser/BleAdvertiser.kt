package de.troido.bleacon.advertiser

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.os.Handler
import android.util.Log
import android.util.SparseArray
import de.troido.bleacon.ble.HandledBleActor
import de.troido.bleacon.scanner.Uuid16
import de.troido.bleacon.util.NORDIC_ID
import de.troido.bleacon.util.bytes
import de.troido.bleacon.util.toHex
import java.util.*

private val header = byteArrayOf(-1)

private const val TAG = "BleAdvertiser"

private fun log(msg: Any?) {
    Log.d(TAG, msg.toString())
}

private fun SparseArray<ByteArray>.print(): String = (0 until size())
        .map { "[${keyAt(it)}]: ${valueAt(it).toHex()}" }
        .joinToString(separator = "\n", prefix = "{\n", postfix = "\n}")

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
                uuid16?.bytes?.let {
                    log("adding uuid16 manufacturer data: ${it.toHex()}")
                    addManufacturerData(NORDIC_ID, header + it)
                }
                uuid128?.bytes?.let {
                    log("adding uuid128 manufacturer data: ${it.toHex()}")
                    addManufacturerData(NORDIC_ID, header + it)
                }
            }
            .build()
            .apply { log("advertising data: ${manufacturerSpecificData.print()}") }

    private val callback = object : AdvertiseCallback() {
        override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) =
                log("advertising successfully started!")

        override fun onStartFailure(errorCode: Int) =
                log("advertising failed with error = $errorCode!")
    }

    private val settings = AdvertiseSettings.Builder()
            .setConnectable(false)
            .apply {
                advertiseMode?.let(this::setAdvertiseMode)
                txPowerLevel?.let(this::setTxPowerLevel)
            }
            .build()

    override fun start() {
        handler.post {
            log("starting advertising...")
            advertiser.startAdvertising(settings, data, data, callback)
        }
    }

    override fun stop() {
        handler.post {
            log("stopping advertising...")
            advertiser.stopAdvertising(callback)
        }
    }
}

private fun obtainAdvertiser(): BluetoothLeAdvertiser {
    val adapter = BluetoothAdapter.getDefaultAdapter()
    if (!adapter.isEnabled) {
        log("enabling bluetooth adapter")
        adapter.enable()
    }

    log("multi-advertising supported = ${adapter.isMultipleAdvertisementSupported}")

    while (true) {
        adapter.bluetoothLeAdvertiser?.let {
            log("obtained advertiser")
            return it
        }
    }
}
