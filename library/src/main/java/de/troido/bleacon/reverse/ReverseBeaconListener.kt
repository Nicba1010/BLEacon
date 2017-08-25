package de.troido.bleacon.reverse

import de.troido.bleacon.scanner.BeaconMetaData

typealias OnReverseBeaconFound<T> = (scanner: ReverseBeacon<T>,
                                     metaData: BeaconMetaData,
                                     data: T) -> Unit

interface ReverseBeaconListener<in T> {
    fun onBeaconFound(scanner: ReverseBeacon<T>,
                      metaData: BeaconMetaData,
                      data: T)
}
