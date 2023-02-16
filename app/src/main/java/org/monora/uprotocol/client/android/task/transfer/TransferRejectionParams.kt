package org.monora.uprotocol.client.android.task.transfer

import org.monora.uprotocol.client.android.database.model.Transfer
import org.monora.uprotocol.core.protocol.Client

class TransferRejectionParams(
    val transfer: Transfer,
    val client: Client,
)
