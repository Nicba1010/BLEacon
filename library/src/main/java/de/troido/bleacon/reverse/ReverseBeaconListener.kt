package de.troido.bleacon.reverse

import de.troido.bleacon.scanner.BeaconMetaData

typealias OnReverseBeaconFound<T> = (ReverseBeacon<T>, BeaconMetaData, T) -> Unit

interface ReverseBeaconListener<T> {
    fun onBeaconFound(scanner: ReverseBeacon<T>,
                      metaData: BeaconMetaData,
                      data: T): Unit
}
