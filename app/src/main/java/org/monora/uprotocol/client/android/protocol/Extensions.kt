package org.monora.uprotocol.client.android.protocol

import org.monora.uprotocol.core.CommunicationBridge
import org.monora.uprotocol.core.protocol.Direction

val CommunicationBridge.cancellationCallback: () -> Unit
    get() = { activeConnection.cancel() }

val Direction.isIncoming
    get() = this == Direction.Incoming

fun CommunicationBridge.closeQuietly() {
    use {

    }
}
