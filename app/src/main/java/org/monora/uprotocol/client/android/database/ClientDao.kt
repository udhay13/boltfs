package org.monora.uprotocol.client.android.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import org.monora.uprotocol.client.android.database.model.UClient

@Dao
interface ClientDao {
    @Delete
    suspend fun delete(client: UClient)

    @Query("SELECT * FROM client WHERE uid = :uid LIMIT 1")
    suspend fun getSingle(uid: String): UClient?

    @Query("SELECT * FROM client WHERE uid = :uid LIMIT 1")
    fun get(uid: String): LiveData<UClient>

    @Query("SELECT * FROM client ORDER BY lastUsageTime DESC")
    fun getAll(): LiveData<List<UClient>>

    @Insert
    suspend fun insert(client: UClient)

    @Update
    suspend fun update(client: UClient)
}