package org.monora.uprotocol.client.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.genonbeta.android.framework.io.DocumentFile
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.monora.uprotocol.client.android.data.ClientRepository
import org.monora.uprotocol.client.android.data.TaskRepository
import org.monora.uprotocol.client.android.data.TransferRepository
import org.monora.uprotocol.client.android.database.model.Transfer
import org.monora.uprotocol.client.android.task.transfer.TransferParams
import org.monora.uprotocol.client.android.task.transfer.TransferRejectionParams
import org.monora.uprotocol.client.android.viewmodel.content.TransferStateContentViewModel

class TransferDetailsViewModel @AssistedInject internal constructor(
    userRepository: ClientRepository,
    taskRepository: TaskRepository,
    private val transferRepository: TransferRepository,
    @Assisted private val transfer: Transfer,
) : ViewModel() {
    val client = userRepository.get(transfer.clientUid)

    val transferDetail = transferRepository.getTransferDetail(transfer.id)

    val state = taskRepository.subscribeToTask {
        if (it.params is TransferParams && it.params.transfer.id == transfer.id) it.params else null
    }.map {
        TransferStateContentViewModel.from(it)
    }

    val rejectionState = taskRepository.subscribeToTask {
        if (it.params is TransferRejectionParams && it.params.transfer.id == transfer.id) it.params else null
    }

    fun getTransferStorage(): DocumentFile {
        return transferRepository.getTransferStorage(transfer)
    }

    fun remove() {
        viewModelScope.launch(Dispatchers.IO) {
            transferRepository.delete(transfer)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(transfer: Transfer): TransferDetailsViewModel
    }

    class ModelFactory(
        private val factory: Factory,
        private val transfer: Transfer,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            check(modelClass.isAssignableFrom(TransferDetailsViewModel::class.java)) {
                "Requested unknown view model type"
            }

            return factory.create(transfer) as T
        }
    }
}
