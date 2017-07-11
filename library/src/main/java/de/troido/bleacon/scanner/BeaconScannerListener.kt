package de.troido.bleacon.scanner

typealias OnBeaconFound<T> = (BeaconScanner<T>, BeaconMetaData, T) -> Unit

interface BeaconScannerListener<T> {
    fun onBeaconFound(scanner: BeaconScanner<T>,
                      metaData: BeaconMetaData,
                      data: T): Unit
}
