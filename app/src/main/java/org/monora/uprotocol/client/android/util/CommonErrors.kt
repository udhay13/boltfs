package org.monora.uprotocol.client.android.util

import android.content.Context
import org.monora.uprotocol.client.android.R
import org.monora.uprotocol.client.android.protocol.NoAddressException
import org.monora.uprotocol.core.io.DefectiveAddressListException
import org.monora.uprotocol.core.protocol.communication.CommunicationException
import org.monora.uprotocol.core.protocol.communication.ContentException
import org.monora.uprotocol.core.protocol.communication.CredentialsException
import org.monora.uprotocol.core.protocol.communication.SecurityException
import org.monora.uprotocol.core.protocol.communication.UndefinedErrorCodeException
import org.monora.uprotocol.core.protocol.communication.client.DifferentRemoteClientException
import org.monora.uprotocol.core.protocol.communication.client.UnauthorizedClientException
import org.monora.uprotocol.core.protocol.communication.client.UntrustedClientException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.ProtocolException

object CommonErrors {
    fun messageOf(context: Context, exception: Exception): String = when (exception) {
        is UntrustedClientException -> context.getString(R.string.error_not_trusted_notice)
        is UnauthorizedClientException -> context.getString(R.string.error_not_allowed)
        is UndefinedErrorCodeException -> context.getString(
            R.string.error_unrecognized, exception.errorCode
        )
        is ContentException -> context.getString(
            when (exception.error) {
                ContentException.Error.NotAccessible -> R.string.error_content_not_accessible
                ContentException.Error.AlreadyExists -> R.string.error_content_already_exists
                ContentException.Error.NotFound -> R.string.error_content_not_found
            }
        )
        is CredentialsException -> context.getString(R.string.error_communication_security_credentials)
        is SecurityException -> context.getString(R.string.error_communication_security)
        is CommunicationException -> context.getString(R.string.error_communication_unknown)
        is NoAddressException -> context.getString(R.string.error_client_no_address)
        is DifferentRemoteClientException -> context.getString(R.string.error_different_client)
        is ProtocolException -> context.getString(R.string.error_protocol_unknown)
        is ConnectException, is DefectiveAddressListException -> context.getString(R.string.error_socket_connection)
        is NoRouteToHostException -> context.getString(R.string.error_no_route_to_host)
        else -> context.getString(R.string.error_unknown)
    }.also {
        exception.printStackTrace()
    }
}
