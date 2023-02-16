package org.monora.uprotocol.client.android.database

import androidx.room.TypeConverter
import org.monora.uprotocol.core.transfer.TransferItem

class TransferItemStateTypeConverter {
    @TypeConverter
    fun fromType(value: TransferItem.State): String = value.toString()

    @TypeConverter
    fun toType(value: String): TransferItem.State = TransferItem.State.valueOf(value)
}