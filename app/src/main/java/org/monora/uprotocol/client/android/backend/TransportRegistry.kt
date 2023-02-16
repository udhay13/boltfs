package org.monora.uprotocol.client.android.backend

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.monora.uprotocol.client.android.database.model.Transfer
import org.monora.uprotocol.core.CommunicationBridge
import org.monora.uprotocol.core.protocol.Direction
import javax.inject.Inject
import javax.inject.Singleton

typealias AcquaintanceCallback = (bridge: CommunicationBridge) -> Unit

typealias TransferRequestCallback = (transfer: Transfer, hasPin: Boolean) -> Unit

@Singleton
class TransportRegistry @Inject constructor(
    private val backend: Backend,
) {
    private var guidanceRequest: GuidanceLifecycleObserver? = null

    private var transferRequest: TransferRequestLifecycleObserver? = null

    fun handleGuidanceRequest(bridge: CommunicationBridge, direction: Direction) {
        val guidanceRequest = guidanceRequest

        if (guidanceRequest != null && guidanceRequest.direction != direction && guidanceRequest.enabled) {
            bridge.activeConnection.isRoaming = true
            backend.applicationScope.launch(Dispatchers.Main) {
                guidanceRequest.callback(bridge)
            }
        } else {
            bridge.send(false)
        }
    }

    fun handleTransferRequest(transfer: Transfer, hasPin: Boolean): Boolean {
        val transferRequest = transferRequest

        if (transferRequest != null && transferRequest.enabled) {
            backend.applicationScope.launch(Dispatchers.Main) {
                transferRequest.callback(transfer, hasPin)
            }
            return true
        }

        return false
    }

    fun registerForGuidanceRequests(
        lifecycleOwner: LifecycleOwner,
        direction: Direction,
        callback: AcquaintanceCallback
    ) {
        lifecycleOwner.lifecycle.addObserver(
            GuidanceLifecycleObserver(lifecycleOwner, direction, callback).also { guidanceRequest = it }
        )
    }

    fun registerForTransferRequests(
        lifecycleOwner: LifecycleOwner,
        callback: TransferRequestCallback,
    ) {
        lifecycleOwner.lifecycle.addObserver(
            TransferRequestLifecycleObserver(lifecycleOwner, callback).also { transferRequest = it }
        )
    }

    internal class TransferRequestLifecycleObserver(
        lifecycleOwner: LifecycleOwner,
        val callback: TransferRequestCallback,
    ) : HandlerLifecycleObserver(lifecycleOwner)

    internal class GuidanceLifecycleObserver(
        lifecycleOwner: LifecycleOwner,
        val direction: Direction,
        val callback: AcquaintanceCallback,
    ) : HandlerLifecycleObserver(lifecycleOwner)

    internal open class HandlerLifecycleObserver(
        private val lifecycleOwner: LifecycleOwner
    ) : LifecycleObserver {
        var enabled = false
            private set

        var destroyed = false
            private set

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun start() {
            enabled = true
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun stop() {
            enabled = false
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun destroy() {
            lifecycleOwner.lifecycle.removeObserver(this)
            destroyed = true
        }
    }
}
