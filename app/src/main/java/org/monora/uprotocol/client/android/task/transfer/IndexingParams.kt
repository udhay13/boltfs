package org.monora.uprotocol.client.android.task.transfer

import org.monora.uprotocol.client.android.database.model.UClient

data class IndexingParams(
    val groupId: Long,
    val client: UClient,
    val jsonData: String,
    val hasPin: Boolean,
)