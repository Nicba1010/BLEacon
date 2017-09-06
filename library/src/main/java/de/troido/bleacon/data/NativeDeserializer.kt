package de.troido.bleacon.data

import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * [Float] deserializer.
 * Assumes [ByteOrder.LITTLE_ENDIAN] endianness.
 */
object Float32Deserializer : BleDeserializer<Float> {
    override val length = 4

    override fun deserialize(data: ByteArray): Float? =
            ByteBuffer.wrap(data, 0, 4).order(ByteOrder.LITTLE_ENDIAN).float
}

/**
 * [Double] deserializer.
 * Assumes [ByteOrder.LITTLE_ENDIAN] endianness.
 */
object Float64Deserializer : BleDeserializer<Double> {
    override val length = 8

    override fun deserialize(data: ByteArray): Double? =
            ByteBuffer.wrap(data, 0, 8).order(ByteOrder.LITTLE_ENDIAN).double
}

/**
 * [Byte] deserializer.
 * Assumes [ByteOrder.LITTLE_ENDIAN] endianness.
 */
object Int8Deserializer : BleDeserializer<Byte> {
    override val length = 1

    override fun deserialize(data: ByteArray): Byte? = data[0]
}

/**
 * [Short] deserializer.
 * Assumes [ByteOrder.LITTLE_ENDIAN] endianness.
 */
object Int16Deserializer : BleDeserializer<Short> {
    override val length = 2

    override fun deserialize(data: ByteArray): Short? =
            ByteBuffer.wrap(data, 0, 2).order(ByteOrder.LITTLE_ENDIAN).short
}

/**
 * [Int] deserializer.
 * Assumes [ByteOrder.LITTLE_ENDIAN] endianness.
 */
object Int32Deserializer : BleDeserializer<Int> {
    override val length = 4

    override fun deserialize(data: ByteArray): Int? =
            ByteBuffer.wrap(data, 0, 4).order(ByteOrder.LITTLE_ENDIAN).int
}

/**
 * [Long] deserializer.
 * Assumes [ByteOrder.LITTLE_ENDIAN] endianness.
 */
object Int64Deserializer : BleDeserializer<Long> {
    override val length = 8

    override fun deserialize(data: ByteArray): Long? =
            ByteBuffer.wrap(data, 0, 8).order(ByteOrder.LITTLE_ENDIAN).long
}
