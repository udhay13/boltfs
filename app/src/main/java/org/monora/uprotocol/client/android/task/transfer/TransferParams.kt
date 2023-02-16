package org.monora.uprotocol.client.android.task.transfer

import androidx.documentfile.provider.DocumentFile
import kotlinx.coroutines.Job
import org.monora.uprotocol.client.android.database.model.Transfer
import org.monora.uprotocol.core.protocol.Client
import org.monora.uprotocol.core.transfer.TransferItem

data class TransferParams(
    val transfer: Transfer,
    val client: Client,
    var bytesTotal: Long,
    var bytesSessionTotal: Long = 0L
) {
    var averageSpeed = 0L

    var bytesOngoing = 0L

    var count = 0

    var job: Job? = null

    var lastFile: DocumentFile? = null

    var ongoing: TransferItem? = null

    val startTime = System.currentTimeMillis()
}
