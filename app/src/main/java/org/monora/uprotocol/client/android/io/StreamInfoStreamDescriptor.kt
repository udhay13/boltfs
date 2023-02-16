package org.monora.uprotocol.client.android.io

import com.genonbeta.android.framework.io.OpenableContent
import org.monora.uprotocol.core.io.StreamDescriptor

class StreamInfoStreamDescriptor(val openableContent: OpenableContent) : StreamDescriptor {
    override fun length(): Long = openableContent.size
}