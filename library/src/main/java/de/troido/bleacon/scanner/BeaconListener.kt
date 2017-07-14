package de.troido.bleacon.scanner

typealias OnBeaconFound<T> = (scanner: BeaconScanner<T>,
                              metaData: BeaconMetaData,
                              data: T) -> Unit

interface BeaconScannerListener<T> {
    fun onBeaconFound(scanner: BeaconScanner<T>,
                      metaData: BeaconMetaData,
                      data: T): Unit
}
