package de.troido.bleacon.data

class UnitDeserializer(override val length: Int = 0) : BleDeserializer<Unit> {
    override fun deserialize(data: ByteArray): Unit? = Unit
}
