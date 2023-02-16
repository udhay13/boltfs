package org.monora.uprotocol.client.android.database.model

import androidx.room.DatabaseView
import org.monora.uprotocol.client.android.model.ListItem
import org.monora.uprotocol.core.protocol.Direction
import org.monora.uprotocol.core.transfer.TransferItem.State.Constants.DONE

@DatabaseView(
    viewName = "transferDetail",
    value = "SELECT transfer.id, transfer.location, transfer.clientUid, transfer.direction, transfer.dateCreated, " +
            "transfer.accepted, client.nickname AS clientNickname, COUNT(items.id) AS itemsCount, " +
            "COUNT(CASE WHEN items.state == '$DONE' THEN items.id END) as itemsDoneCount, SUM(items.size) AS size, " +
            "SUM(CASE WHEN items.state == '$DONE' THEN items.size END) as sizeOfDone FROM transfer " +
            "INNER JOIN client ON client.uid = transfer.clientUid " +
            "INNER JOIN transferItem items ON items.groupId = transfer.id GROUP BY items.groupId"
)
data class TransferDetail(
    val id: Long,
    val clientUid: String,
    val clientNickname: String,
    val location: String,
    val direction: Direction,
    val size: Long,
    val accepted: Boolean,
    val sizeOfDone: Long,
    val itemsCount: Int,
    val itemsDoneCount: Int,
    val dateCreated: Long,
) : ListItem {
    override val listId: Long
        get() = id + javaClass.hashCode()
}
