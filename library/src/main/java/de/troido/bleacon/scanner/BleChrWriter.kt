package de.troido.bleacon.scanner

interface BleChrWriter {
    fun write(value: ByteArray)
    fun close()
}
