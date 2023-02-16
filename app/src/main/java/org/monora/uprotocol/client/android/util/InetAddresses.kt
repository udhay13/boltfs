package org.monora.uprotocol.client.android.util

import java.io.IOException
import java.net.InetAddress

object InetAddresses {
    @Throws(IOException::class)
    fun from(address: Int): InetAddress = InetAddress.getByAddress(
        byteArrayOf(
            address.toByte(),
            (address ushr 8).toByte(),
            (address ushr 16).toByte(),
            (address ushr 24).toByte()
        )
    )
}