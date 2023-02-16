package org.monora.uprotocol.client.android.viewmodel.content

import androidx.lifecycle.LiveData

data class TransferStateFeederViewModel(val state: LiveData<TransferStateContentViewModel>)