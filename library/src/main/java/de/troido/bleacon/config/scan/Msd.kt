package de.troido.bleacon.config.scan

@Suppress("ArrayInDataClass")
data class Msd(val id: Int, val mask: ByteArray, val data: ByteArray)
