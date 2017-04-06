package de.troido.bleacon.data

/**
 * BLE data deserializer.
 * Can be seen as a function from a [ByteArray] to some nullable [T], where `null` indicates a
 * parse error.
 *
 * @property[length] The size of the byte array this deserializer takes in.
 * Any function supplying data to [deserialize] **MUST** provide an array of this exact length.
 */
interface BleDeserializer<out T> {
    val length: Int

    /**
     * Deserializes the given byte array, returning `null` in case of a parse error.
     * The implementing class can assume that the supplied array's length **WILL** match [length].
     */
    fun deserialize(data: ByteArray): T?
}

/**
 * Returns a deserializer which maps the output of `this` deserializer through the given
 * function [f].
 */
fun <A, B> BleDeserializer<A>.mapping(f: (A) -> B?): BleDeserializer<B> =
        object : BleDeserializer<B> {
            override val length = this@mapping.length
            override fun deserialize(data: ByteArray): B? =
                    this@mapping.deserialize(data)?.let(f)
        }

/**
 * Returns a deserializer which maps the output of `this` deserializer through the given
 * function [f].
 * Since this deserializer's function's argument is nullable, it can, in contrast to [mapping]
 * recover from a parsing error, hence its name.
 */
fun <A, B> BleDeserializer<A>.recoverableMapping(f: (A?) -> B?): BleDeserializer<B> =
        object : BleDeserializer<B> {
            override val length = this@recoverableMapping.length
            override fun deserialize(data: ByteArray): B? =
                    this@recoverableMapping.deserialize(data).let(f)
        }
