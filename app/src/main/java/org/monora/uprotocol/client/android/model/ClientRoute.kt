package org.monora.uprotocol.client.android.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.monora.uprotocol.client.android.database.model.UClient
import org.monora.uprotocol.client.android.database.model.UClientAddress

@Parcelize
data class ClientRoute(var client: UClient, var address: UClientAddress) : Parcelable