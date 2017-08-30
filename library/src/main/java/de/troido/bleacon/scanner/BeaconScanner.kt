package de.troido.bleacon.scanner

import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.os.Handler
import de.troido.bleacon.ble.HandledBleActor
import de.troido.bleacon.ble.NORDIC_ID
import de.troido.bleacon.ble.obtainScanner
import de.troido.bleacon.config.scan.scanSettings
import de.troido.bleacon.data.BleDeserializer
import de.troido.bleacon.data.matchesLength

/**
 * @param[handler] optional handler for sharing with other asynchronous actions.
 */
class BeaconScanner<out T>(
        private val deserializer: BleDeserializer<T>,
        filter: ScanFilter,
        dataTransform: (ByteArray) -> ByteArray = { it },
        private val settings: ScanSettings = scanSettings(),
        handler: Handler = Handler(),
        private val onDeviceFound: OnBeaconFound<T>
) : HandledBleActor(handler) {

    @JvmOverloads constructor(
            deserializer: BleDeserializer<T>,
            filter: ScanFilter,
            dataTransform: (ByteArray) -> ByteArray = { it },
            settings: ScanSettings = scanSettings(),
            handler: Handler = Handler(),
            beaconListener: BeaconScannerListener<T>
    ) : this(deserializer, filter, dataTransform, settings, handler, beaconListener::onBeaconFound)

    private val scanner = obtainScanner()
    private val filters = listOf(filter)

    private val callback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            result?.run {
                scanRecord
                        ?.getManufacturerSpecificData(NORDIC_ID)
                        ?.let(dataTransform)
                        ?.takeIf { deserializer.matchesLength(it) }
                        ?.let {
                            when (deserializer.length) {
                                BleDeserializer.ALL -> it
                                else                -> it.copyOfRange(0, deserializer.length)
                            }
                        }
                        ?.let(deserializer::deserialize)
                        ?.let {
                            onDeviceFound(this@BeaconScanner,
                                          BeaconMetaData(device,
                                                         result.rssi,
                                                         result.scanRecord.txPowerLevel),
                                          it)
                        }
            }
        }
    }

    override fun start() {
        handler.post {
            scanner.startScan(filters, settings, callback)
        }
    }

    override fun stop() {
        handler.post { scanner.stopScan(callback) }
    }
}
