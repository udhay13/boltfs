package org.monora.uprotocol.client.android.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.monora.uprotocol.client.android.backend.AcquaintanceCallback
import org.monora.uprotocol.client.android.backend.TransferRequestCallback
import org.monora.uprotocol.client.android.backend.TransportRegistry
import org.monora.uprotocol.client.android.lifecycle.SingleLiveEvent
import org.monora.uprotocol.core.CommunicationBridge
import org.monora.uprotocol.core.protocol.Direction
import javax.inject.Inject

@HiltViewModel
class ClientPickerViewModel @Inject internal constructor(
    private val transportRegistry: TransportRegistry,
) : ViewModel() {
    val bridge = SingleLiveEvent<CommunicationBridge>()

    fun registerForGuidanceRequests(
        lifecycleOwner: LifecycleOwner,
        direction: Direction,
        callback: AcquaintanceCallback
    ) {
        transportRegistry.registerForGuidanceRequests(lifecycleOwner, direction, callback)
    }

    fun registerForTransferRequests(
        lifecycleOwner: LifecycleOwner,
        callback: TransferRequestCallback,
    ) {
        transportRegistry.registerForTransferRequests(lifecycleOwner, callback)
    }
}
