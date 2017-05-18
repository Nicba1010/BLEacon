package de.troido.bleacon.data

import java.nio.ByteBuffer
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
        object Deserializer : BleDeserializer<Float32> {
            override val length = 4

            override fun deserialize(data: ByteArray): Float32? =
                    Float32(ByteBuffer.wrap(data, 0, 4).order(ByteOrder.LITTLE_ENDIAN).float)
        }
    }

    data class Float64(val data: Double) : Primitive() {

        /**
         * [Float64] deserializer.
         * Assumes [ByteOrder.LITTLE_ENDIAN] endianness.
         */
        object Deserializer : BleDeserializer<Float64> {
            override val length = 8

            override fun deserialize(data: ByteArray): Float64? =
                    Float64(ByteBuffer.wrap(data, 0, 8).order(ByteOrder.LITTLE_ENDIAN).double)
        }
    }

    data class Int8(val data: Byte) : Primitive() {

        /**
         * [Int8] deserializer.
         * Assumes [ByteOrder.LITTLE_ENDIAN] endianness.
         */
        object Deserializer : BleDeserializer<Int8> {
            override val length = 1

            override fun deserialize(data: ByteArray): Int8? = Int8(data[0])
        }
    }

    data class Int16(val data: Short) : Primitive() {

        /**
         * [Int16] deserializer.
         * Assumes [ByteOrder.LITTLE_ENDIAN] endianness.
         */
        object Deserializer : BleDeserializer<Int16> {
            override val length = 2

            override fun deserialize(data: ByteArray): Int16? =
                    Int16(ByteBuffer.wrap(data, 0, 2).order(ByteOrder.LITTLE_ENDIAN).short)
        }
    }

    data class Int32(val data: Int) : Primitive() {

        /**
         * [Int32] deserializer.
         * Assumes [ByteOrder.LITTLE_ENDIAN] endianness.
         */
        object Deserializer : BleDeserializer<Int32> {
            override val length = 4

            override fun deserialize(data: ByteArray): Int32? =
                    Int32(ByteBuffer.wrap(data, 0, 4).order(ByteOrder.LITTLE_ENDIAN).int)
        }
    }

    data class Int64(val data: Long) : Primitive() {

        /**
         * [Int64] deserializer.
         * Assumes [ByteOrder.LITTLE_ENDIAN] endianness.
         */
        object Deserializer : BleDeserializer<Int64> {
            override val length = 8

            override fun deserialize(data: ByteArray): Int64? =
                    Int64(ByteBuffer.wrap(data, 0, 8).order(ByteOrder.LITTLE_ENDIAN).long)
        }
    }
}
