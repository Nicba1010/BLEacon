package de.troido.bleacon.data

import java.nio.ByteBuffer
import java.nio.ByteOrder

sealed class Primitive {
    object Unit : Primitive() {
        object Deserializer : BleDeserializer<Unit> {
            override val length = 0

            override fun deserialize(data: ByteArray): Unit? = Unit
        }
    }

    data class Float32(val data: Float) : Primitive() {
        object Deserializer : BleDeserializer<Float32> {
            override val length = 4

            override fun deserialize(data: ByteArray): Float32? =
                    Float32(ByteBuffer.wrap(data, 0, 4).order(ByteOrder.LITTLE_ENDIAN).float)
        }
    }

    data class Int8(val data: Byte) : Primitive() {
        object Deserializer : BleDeserializer<Int8> {
            override val length = 1

            override fun deserialize(data: ByteArray): Int8? = Int8(data[0])
        }
    }

    data class Int16(val data: Short) : Primitive() {
        object Deserializer : BleDeserializer<Int16> {
            override val length = 2

            override fun deserialize(data: ByteArray): Int16? =
                    Int16(ByteBuffer.wrap(data, 0, 2).order(ByteOrder.LITTLE_ENDIAN).short)
        }
    }
}