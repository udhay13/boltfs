package org.monora.uprotocol.client.android.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.monora.uprotocol.client.android.data.TransferTaskRepository
import org.monora.uprotocol.client.android.database.model.Transfer
import org.monora.uprotocol.client.android.database.model.TransferDetail
import org.monora.uprotocol.core.protocol.Client
import javax.inject.Inject

@HiltViewModel
class TransferManagerViewModel @Inject internal constructor(
    private val transferTaskRepository: TransferTaskRepository,
) : ViewModel() {
    fun rejectTransferRequest(transfer: Transfer, client: Client) {
        transferTaskRepository.rejectTransfer(transfer, client)
    }

    fun toggleTransferOperation(transfer: Transfer, client: Client, detail: TransferDetail) {
        transferTaskRepository.toggleTransferOperation(transfer, client, detail)
    }
}
