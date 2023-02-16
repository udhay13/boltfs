package org.monora.uprotocol.client.android.database.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.*
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import org.monora.uprotocol.client.android.model.ListItem

@Parcelize
@Entity(
    tableName = "sharedText",
    foreignKeys = [
        ForeignKey(
            entity = UClient::class, parentColumns = ["uid"], childColumns = ["clientUid"], onDelete = CASCADE
        ),
    ]
)
data class SharedText(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val clientUid: String?,
    var text: String,
    val created: Long = System.currentTimeMillis(),
    var modified: Long = created,
) : Parcelable, ListItem {
    @IgnoredOnParcel
    override val listId: Long
        get() = id.toLong() + created
}
