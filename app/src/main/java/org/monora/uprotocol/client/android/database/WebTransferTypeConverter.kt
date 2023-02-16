package org.monora.uprotocol.client.android.database

import android.net.Uri
import androidx.room.TypeConverter

class WebTransferTypeConverter {
    @TypeConverter
    fun fromType(uri: Uri): String = uri.toString()

    @TypeConverter
    fun toType(uriString: String): Uri = Uri.parse(uriString)
}
