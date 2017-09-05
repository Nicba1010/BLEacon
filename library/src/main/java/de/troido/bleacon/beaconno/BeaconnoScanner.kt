package de.troido.bleacon.beaconno

import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.Handler
import de.troido.bleacon.ble.HandledBleActor
import de.troido.bleacon.ble.obtainScanner
import de.troido.bleacon.config.scan.scanSettings
import de.troido.bleacon.data.BleDeserializer
import de.troido.bleacon.data.UndefinedDeserializer
import de.troido.bleacon.scanner.BeaconMetaData

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
                         deserializer: BleDeserializer<T> = UndefinedDeserializer(),
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
