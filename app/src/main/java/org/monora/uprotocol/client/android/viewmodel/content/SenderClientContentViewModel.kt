package org.monora.uprotocol.client.android.viewmodel.content

import org.monora.uprotocol.core.protocol.Client

class SenderClientContentViewModel(val client: Client?) {
    val hasClient = client != null

    val nickname = client?.clientNickname
}
