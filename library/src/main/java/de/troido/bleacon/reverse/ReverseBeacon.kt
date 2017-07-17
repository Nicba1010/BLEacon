package de.troido.bleacon.reverse

import android.os.Handler
import de.troido.bleacon.advertiser.BleAdvertiser
import de.troido.bleacon.ble.BleActor
import de.troido.bleacon.ble.HandledBleActor
import de.troido.bleacon.config.BleAdData
import de.troido.bleacon.config.BleAdSettings
import de.troido.bleacon.config.BleFilter
import de.troido.bleacon.config.BleScanSettings
import de.troido.bleacon.data.BleDeserializer
import de.troido.bleacon.scanner.BeaconScanner
import de.troido.bleacon.util.Uuid16
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
        scanSettings: BleScanSettings = BleScanSettings(),
        adSettings: BleAdSettings = BleAdSettings(),
        handler: Handler = Handler(),
        onDeviceFound: OnReverseBeaconFound<T>
) : HandledBleActor(handler) {

    @JvmOverloads constructor(
            deserializer: BleDeserializer<T>,
            uuid16: Uuid16? = null,
            uuid128: UUID? = null,
            scanSettings: BleScanSettings = BleScanSettings(),
            adSettings: BleAdSettings = BleAdSettings(),
            handler: Handler = Handler(),
            reverseBeaconListener: ReverseBeaconListener<T>
    ) : this(deserializer, uuid16, uuid128, scanSettings, adSettings, handler,
             reverseBeaconListener::onBeaconFound)

    private val scanner = BeaconScanner(
            deserializer,
            BleFilter(
                uuid16 = this@ReverseBeacon.uuid16,
                uuid128 = this@ReverseBeacon.uuid128
            ),
            scanSettings,
            handler
    ) { _, device, data -> onDeviceFound(this, device, data) }

    private val advertiser = BleAdvertiser(
            BleAdData(
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
