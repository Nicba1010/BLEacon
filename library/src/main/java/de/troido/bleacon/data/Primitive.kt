package de.troido.bleacon.data

import java.nio.ByteOrder

/**
 * Wrappers and deserializers for some primitive data types.
 * All deserializers assume [ByteOrder.LITTLE_ENDIAN] endianness.
 */
sealed class Primitive {
    object Unit : Primitive() {
        object Deserializer : BleDeserializer<Unit> {
            override val length = 0

            override fun deserialize(data: ByteArray): Unit? = Unit
        }
    }

    data class Float32(val data: Float) : Primitive() {
        /**
         * [Float32] deserializer.
         * Assumes [ByteOrder.LITTLE_ENDIAN] endianness.
         */
        object Deserializer : BleDeserializer<Float32> by Float32Deserializer.mapping(::Float32)
    }

    data class Float64(val data: Double) : Primitive() {
        /**
         * [Float64] deserializer.
         * Assumes [ByteOrder.LITTLE_ENDIAN] endianness.
         */
        object Deserializer : BleDeserializer<Float64> by Float64Deserializer.mapping(::Float64)
    }

    data class Int8(val data: Byte) : Primitive() {

        /**
         * [Int8] deserializer.
         * Assumes [ByteOrder.LITTLE_ENDIAN] endianness.
         */
        object Deserializer : BleDeserializer<Int8> by Int8Deserializer.mapping(::Int8)
    }

    data class Int16(val data: Short) : Primitive() {

        /**
         * [Int16] deserializer.
         * Assumes [ByteOrder.LITTLE_ENDIAN] endianness.
         */
        object Deserializer : BleDeserializer<Int16> by Int16Deserializer.mapping(::Int16)
    }

    data class Int32(val data: Int) : Primitive() {

        /**
         * [Int32] deserializer.
         * Assumes [ByteOrder.LITTLE_ENDIAN] endianness.
         */
        object Deserializer : BleDeserializer<Int32> by Int32Deserializer.mapping(::Int32)
    }

    data class Int64(val data: Long) : Primitive() {

        /**
         * [Int64] deserializer.
         * Assumes [ByteOrder.LITTLE_ENDIAN] endianness.
         */
        object Deserializer : BleDeserializer<Int64> by Int64Deserializer.mapping(::Int64)
    }
}
