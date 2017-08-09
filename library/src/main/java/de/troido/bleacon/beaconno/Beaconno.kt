package de.troido.bleacon.beaconno

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanRecord
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.Handler
import android.util.Log
import de.troido.bleacon.ble.HandledBleActor
import de.troido.bleacon.ble.NORDIC_ID
import de.troido.bleacon.ble.obtainScanner
import de.troido.bleacon.config.scan.scanSettings
import de.troido.bleacon.data.BleDeserializer
import de.troido.bleacon.data.NullDeserializer
import de.troido.bleacon.scanner.BeaconMetaData
import de.troido.bleacon.scanner.BleChrWriter
import java.util.UUID

typealias OnBeaconno<T> = (scanner: BeaconnoScanner<T>, device: BeaconnoDevice<T>) -> Unit
typealias OnBeaconnoData<T> = (data: T) -> Unit
typealias OnBeaconnoWriter = (writer: BleChrWriter) -> Unit


fun bleConnectionCallback(svcUuid: UUID,
                          chrUuid: UUID,
                          onBeaconnoWriter: OnBeaconnoWriter
): BluetoothGattCallback = object : BluetoothGattCallback() {

    override fun onConnectionStateChange(gatt: BluetoothGatt?,
                                         status: Int,
                                         newState: Int) {
        super.onConnectionStateChange(gatt, status, newState)

        Log.d("DumbDebug", newState.toString())

        when (newState) {
            BluetoothProfile.STATE_CONNECTED    -> gatt?.discoverServices()
            BluetoothProfile.STATE_DISCONNECTED -> gatt?.close()
        }
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
        super.onServicesDiscovered(gatt, status)

        gatt?.getService(svcUuid)
                ?.getCharacteristic(chrUuid)
                ?.let { chr ->
                    chr.writeType = BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
                    onBeaconnoWriter(BleChrWriter(chr, gatt))
                }
    }
}


class BeaconnoDevice<out T>(private val context: Context,
                            private val deserializer: BleDeserializer<T>,
                            private val dataTransform: (ByteArray) -> ByteArray,
                            private val scanRecord: ScanRecord,
                            val metaData: BeaconMetaData) {

    val data: T? by lazy {
        scanRecord.getManufacturerSpecificData(NORDIC_ID)
                ?.let(dataTransform)
                ?.let {
                    when (deserializer.length) {
                        BleDeserializer.ALL -> it
                        in 0..it.size       -> it.copyOfRange(0, deserializer.length)
                        else                -> null
                    }
                }
                ?.let(deserializer::deserialize)
    }

    fun connect(context: Context, callback: BluetoothGattCallback) {
        metaData.device.connectGatt(context, false, callback)
    }
}

/**
 * Example usage:
 * ```
 * BeaconnoScanner(
 *         context,
 *         bleFilter(uuid16 = Uuid16.fromString("17CF")),
 *         Int16Deserializer,
 *         UUID16_TRANSFORM,
 *         scanSettings()
 * ) { scanner, device ->
 *     device.connect(
 *             context,
 *             bleConnectionCallback(
 *                     Uuid16.fromString("23FF").toUuid(),
 *                     Uuid16.fromString("4566").toUuid()
 *             ) { writer ->
 *                 writer.write("Hello ${device.data}!".toByteArray())
 *             }
 *     )
 *     Log.d("Beaconno", "Found a beaconno with ${device.data}!")
 * }
 * ```
 */
class BeaconnoScanner<T>(context: Context,
                         filter: ScanFilter,
                         deserializer: BleDeserializer<T> = NullDeserializer(),
                         dataTransform: (ByteArray) -> ByteArray,
                         private val settings: ScanSettings = scanSettings(),
                         handler: Handler = Handler(),
                         private val listener: OnBeaconno<T>
) : HandledBleActor(handler) {

    private val scanner = obtainScanner()
    private val filters = listOf(filter)

    private val callback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            result.scanRecord
                    ?.let {
                        BeaconnoDevice(context, deserializer, dataTransform, it,
                                       BeaconMetaData(result.device, result.rssi, it.txPowerLevel))
                    }
                    ?.let { listener(this@BeaconnoScanner, it) }
        }
    }

    override fun start() {
        handler.post { scanner.startScan(filters, settings, callback) }
    }

    override fun stop() {
        handler.post { scanner.stopScan(callback) }
    }
}
