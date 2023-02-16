package org.monora.uprotocol.client.android.database

import androidx.room.TypeConverter
import org.monora.uprotocol.core.protocol.Direction

class DirectionTypeConverter {
    @TypeConverter
    fun fromType(value: Direction): String = value.protocolValue

    @TypeConverter
    fun toType(value: String): Direction = Direction.from(value)
}
