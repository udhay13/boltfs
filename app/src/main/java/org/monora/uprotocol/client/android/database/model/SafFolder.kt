package org.monora.uprotocol.client.android.database.model

import android.net.Uri
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "safFolder"
)
data class SafFolder(
    @PrimaryKey
    val uri: Uri,
    val name: String,
) : Parcelable
