package org.monora.uprotocol.client.android.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.monora.uprotocol.client.android.database.model.SafFolder
import org.monora.uprotocol.client.android.database.model.SharedText
import org.monora.uprotocol.client.android.database.model.Transfer
import org.monora.uprotocol.client.android.database.model.TransferDetail
import org.monora.uprotocol.client.android.database.model.UClient
import org.monora.uprotocol.client.android.database.model.UClientAddress
import org.monora.uprotocol.client.android.database.model.UTransferItem
import org.monora.uprotocol.client.android.database.model.WebClient
import org.monora.uprotocol.client.android.database.model.WebTransfer

@Database(
    entities = [
        UClient::class,
        UClientAddress::class,
        UTransferItem::class,
        SafFolder::class,
        SharedText::class,
        Transfer::class,
        WebClient::class,
        WebTransfer::class,
    ],
    views = [TransferDetail::class],
    version = 1
)
@TypeConverters(
    ClientTypeConverter::class, IOTypeConverter::class, DirectionTypeConverter::class,
    TransferItemStateTypeConverter::class, WebTransferTypeConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clientDao(): ClientDao

    abstract fun clientAddressDao(): ClientAddressDao

    abstract fun safFolderDao(): SafFolderDao

    abstract fun sharedTextDao(): SharedTextDao

    abstract fun transferDao(): TransferDao

    abstract fun transferItemDao(): TransferItemDao

    abstract fun webClientDao(): WebClientDao

    abstract fun webTransferDao(): WebTransferDao
}
