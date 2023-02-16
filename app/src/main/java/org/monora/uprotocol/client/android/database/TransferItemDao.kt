package org.monora.uprotocol.client.android.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import org.monora.uprotocol.client.android.database.model.UTransferItem
import org.monora.uprotocol.core.protocol.Direction
import org.monora.uprotocol.core.transfer.TransferItem.State.Constants.PENDING

@Dao
interface TransferItemDao {
    @Delete
    suspend fun delete(transferItem: UTransferItem)

    @Query("SELECT * FROM transferItem WHERE groupId = :groupId ORDER BY name")
    fun getAll(groupId: Long): LiveData<List<UTransferItem>>

    @Query("SELECT * FROM transferItem WHERE groupId = :groupId AND state == '$PENDING' ORDER BY name LIMIT 1")
    suspend fun getReceivable(groupId: Long): UTransferItem?

    @Query("SELECT * FROM transferItem WHERE groupId = :groupId AND id = :id AND direction = :direction LIMIT 1")
    suspend fun get(groupId: Long, id: Long, direction: Direction): UTransferItem?

    @Query("SELECT * FROM transferItem WHERE location = :location AND direction = :direction")
    suspend fun get(location: String, direction: Direction): UTransferItem?

    @Insert
    suspend fun insert(list: List<UTransferItem>)

    @Update
    suspend fun update(transferItem: UTransferItem)
}
