package de.troido.bleacon.reverse

import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.ScanSettings
import android.os.Handler
import de.troido.bleacon.advertiser.BleAdvertiser
import de.troido.bleacon.ble.BleActor
import de.troido.bleacon.ble.HandledBleActor
import de.troido.bleacon.config.advertise.adSettings
import de.troido.bleacon.config.advertise.bleAdData
import de.troido.bleacon.config.scan.IDENTITY
import de.troido.bleacon.config.scan.UUID128_TRANSFORM
import de.troido.bleacon.config.scan.UUID16_TRANSFORM
import de.troido.bleacon.config.scan.bleFilter
import de.troido.bleacon.config.scan.scanSettings
import de.troido.bleacon.data.BleDeserializer
import de.troido.bleacon.scanner.BeaconScanner
import de.troido.ekstend.uuid.Uuid16
import java.util.UUID

/**
 * A “reverse” beacon, in the sense that the device both advertises that it's looking for the
 * beacon and scans for the said beacon, while the beacon scans for such advertisements and
 * advertises itself for a short amount of time after a successful scan.
 *
 * [uuid16] or [uuid128] specify the beacon's UUID, if both are provided, then only [uuid128] is
 * taken into account.
 *
 * @param[handler] optional handler for sharing with other asynchronous actions.
 */
class ReverseBeacon<out T>(
        deserializer: BleDeserializer<T>,
        private val uuid16: Uuid16? = null,
        private val uuid128: UUID? = null,
        scanSettings: ScanSettings = scanSettings(),
        adSettings: AdvertiseSettings = adSettings(),
        handler: Handler = Handler(),
        onDeviceFound: OnReverseBeaconFound<T>
) : HandledBleActor(handler) {

    @JvmOverloads constructor(
            deserializer: BleDeserializer<T>,
            uuid16: Uuid16? = null,
            uuid128: UUID? = null,
            scanSettings: ScanSettings = scanSettings(),
            adSettings: AdvertiseSettings = adSettings(),
            handler: Handler = Handler(),
            reverseBeaconListener: ReverseBeaconListener<T>
    ) : this(deserializer, uuid16, uuid128, scanSettings, adSettings, handler,
             reverseBeaconListener::onBeaconFound)

    private val scanner = BeaconScanner(
            deserializer,
            bleFilter(
                    uuid16 = this@ReverseBeacon.uuid16,
                    uuid128 = this@ReverseBeacon.uuid128
            ),
            when {
                uuid16 != null  -> UUID16_TRANSFORM
                uuid128 != null -> UUID128_TRANSFORM
                else            -> IDENTITY
            },
            scanSettings,
            handler
    ) { _, device, data -> onDeviceFound(this, device, data) }

    private val advertiser = BleAdvertiser(
            bleAdData(
                    uuid16 = this@ReverseBeacon.uuid16,
                    uuid128 = this@ReverseBeacon.uuid128
            ),
            adSettings,
            handler = handler
    )

    private val actors = arrayOf(scanner, advertiser)

    override fun start() {
        handler.post { actors.forEach(BleActor::start) }
    }

    override fun stop() {
        handler.post { actors.forEach(BleActor::stop) }
    }
}
