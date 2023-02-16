package org.monora.uprotocol.client.android.model

import android.os.Parcelable
import androidx.core.util.ObjectsCompat
import kotlinx.parcelize.Parcelize

@Parcelize
class NetworkDescription(var ssid: String, var bssid: String?, var password: String?) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (other is NetworkDescription) {
            return bssid == other.bssid || (bssid == null && ssid == other.ssid)
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return ObjectsCompat.hash(ssid, bssid, password)
    }
}
