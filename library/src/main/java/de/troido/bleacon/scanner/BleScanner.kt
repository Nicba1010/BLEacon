package de.troido.bleacon.scanner

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.Handler
import de.troido.bleacon.ble.HandledBleActor
import de.troido.bleacon.ble.obtainScanner
import de.troido.bleacon.config.scan.scanSettings
import java.util.UUID

/**
 * A more idiomatic wrapper for [android.bluetooth.le.BluetoothLeScanner].
 *
 * @param[svcUuid] UUID of the target service.
 *
 * @param[chrUuid] UUID of the target characteristic of the target service.
 *
 * @param[autoConnect] if `false`, directly connect to the remote device, or if `true`,
 * automatically connect as soon as the remote device becomes available.
 * See [android.bluetooth.BluetoothDevice.connectGatt]'s `autoConnect` param for more details.
 *
 * @param[stopWhenFound] if `true` then the scanner will be automatically stopped on first result.
 *
 * @param[handler] optional handler for sharing with other asynchronous actions.
 */
class BleScanner(context: Context,
                 filter: ScanFilter,
                 svcUuid: UUID,
                 chrUuid: UUID,
                 callback: BleScanCallback,
                 private val settings: ScanSettings = scanSettings(),
                 autoConnect: Boolean = false,
                 stopWhenFound: Boolean = true,
                 handler: Handler = Handler()
) : HandledBleActor(handler) {

    private val scanner = obtainScanner()

    private val filters = listOf(filter)

    private val gattCallback = callback.toBtGattCallback(svcUuid, chrUuid, this)

    private val scanCallback = object : ScanCallback() {
        private fun connectDevice(device: BluetoothDevice) {
            if (stopWhenFound) {
                scanner.stopScan(this)
            }

            device.connectGatt(context, autoConnect, gattCallback)
        }

        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            result?.device?.let(this::connectDevice)
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)
            results?.mapNotNull { it.device }?.firstOrNull()?.let(this::connectDevice)
        }
    }

    override fun start() {
        handler.post { scanner.startScan(filters, settings, scanCallback) }
    }

    override fun stop() {
        handler.post { scanner.stopScan(null) }
    }
}
