package org.monora.uprotocol.client.android.database.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import kotlinx.parcelize.Parcelize
import org.monora.uprotocol.core.protocol.Direction
import org.monora.uprotocol.core.transfer.TransferItem

@Parcelize
@Entity(
    tableName = "transferItem",
    primaryKeys = ["groupId", "id"],
    foreignKeys = [
        ForeignKey(
            entity = Transfer::class, parentColumns = ["id"], childColumns = ["groupId"], onDelete = CASCADE
        ),
    ]
)
data class UTransferItem(
    var id: Long,
    var groupId: Long,
    var name: String,
    var mimeType: String,
    var size: Long,
    var directory: String?,
    var location: String,
    var direction: Direction,
    var state: TransferItem.State = TransferItem.State.Pending,
    var dateCreated: Long = System.currentTimeMillis(),
    var dateModified: Long = dateCreated,
) : TransferItem, Parcelable {
    override fun getItemDirection(): Direction = direction

    override fun getItemDirectory(): String? = directory

    override fun getItemGroupId(): Long = groupId

    override fun getItemId(): Long = id

    override fun getItemLastChangeTime(): Long = dateModified

    override fun getItemMimeType(): String = mimeType

    override fun getItemName(): String = name

    override fun getItemSize(): Long = size

    override fun setItemDirection(direction: Direction) {
        this.direction = direction
    }

    override fun setItemId(id: Long) {
        this.id = id
    }

    override fun setItemGroupId(groupId: Long) {
        this.groupId = groupId
    }

    override fun setItemName(name: String) {
        this.name = name
    }

    override fun setItemDirectory(directory: String?) {
        this.directory = directory
    }

    override fun setItemMimeType(mimeType: String) {
        this.mimeType = mimeType
    }

    override fun setItemSize(size: Long) {
        this.size = size
    }

    override fun setItemLastChangeTime(lastChangeTime: Long) {
        this.dateModified = lastChangeTime
    }
}
