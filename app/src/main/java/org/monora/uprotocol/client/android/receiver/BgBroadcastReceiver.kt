package org.monora.uprotocol.client.android.receiver

import android.content.BroadcastReceiver
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.LifecycleService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.monora.uprotocol.client.android.R
import org.monora.uprotocol.client.android.backend.Backend
import org.monora.uprotocol.client.android.data.ClientRepository
import org.monora.uprotocol.client.android.data.TransferRepository
import org.monora.uprotocol.client.android.data.TransferTaskRepository
import org.monora.uprotocol.client.android.database.model.SharedText
import org.monora.uprotocol.client.android.database.model.Transfer
import org.monora.uprotocol.client.android.database.model.UClient
import org.monora.uprotocol.client.android.util.NotificationBackend
import org.monora.uprotocol.core.TransportSeat
import org.monora.uprotocol.core.persistence.PersistenceProvider
import org.monora.uprotocol.core.protocol.ConnectionFactory
import javax.inject.Inject

@AndroidEntryPoint
class BgBroadcastReceiver : BroadcastReceiver() {
    @Inject
    lateinit var backend: Backend

    @Inject
    lateinit var clientRepository: ClientRepository

    @Inject
    lateinit var connectionFactory: ConnectionFactory

    @Inject
    lateinit var persistenceProvider: PersistenceProvider

    @Inject
    lateinit var transferRepository: TransferRepository

    @Inject
    lateinit var transferTaskRepository: TransferTaskRepository

    @Inject
    lateinit var transportSeat: TransportSeat

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_FILE_TRANSFER -> {
                val client: UClient? = intent.getParcelableExtra(EXTRA_CLIENT)
                val transfer: Transfer? = intent.getParcelableExtra(EXTRA_TRANSFER)
                val notificationId = intent.getIntExtra(NotificationBackend.EXTRA_NOTIFICATION_ID, -1)
                val isAccepted = intent.getBooleanExtra(EXTRA_ACCEPTED, false)

                backend.services.notifications.backend.cancel(notificationId)

                if (client != null && transfer != null) {
                    if (isAccepted) backend.applicationScope.launch(Dispatchers.IO) {
                        transferRepository.getTransferDetailDirect(transfer.id)?.let { transferDetail ->
                            transferTaskRepository.toggleTransferOperation(transfer, client, transferDetail)
                        }
                    } else {
                        transferTaskRepository.rejectTransfer(transfer, client)
                    }
                }
            }
            ACTION_DEVICE_KEY_CHANGE_APPROVAL -> {
                val client: UClient? = intent.getParcelableExtra(EXTRA_CLIENT)
                val notificationId = intent.getIntExtra(NotificationBackend.EXTRA_NOTIFICATION_ID, -1)

                backend.services.notifications.backend.cancel(notificationId)

                if (client != null && intent.getBooleanExtra(EXTRA_ACCEPTED, false)) {
                    persistenceProvider.approveInvalidationOfCredentials(client)
                }
            }
            ACTION_CLIPBOARD_COPY -> {
                val notificationId = intent.getIntExtra(NotificationBackend.EXTRA_NOTIFICATION_ID, -1)
                val sharedText: SharedText? = intent.getParcelableExtra(EXTRA_SHARED_TEXT)

                backend.services.notifications.backend.cancel(notificationId)

                if (sharedText != null) {
                    val cbManager = context.applicationContext.getSystemService(
                        LifecycleService.CLIPBOARD_SERVICE
                    ) as ClipboardManager
                    cbManager.setPrimaryClip(ClipData.newPlainText("receivedText", sharedText.text))
                    Toast.makeText(context, R.string.copy_text_to_clipboard_success, Toast.LENGTH_SHORT).show()
                }
            }
            ACTION_STOP_ALL_TASKS -> backend.cancelAllTasks()
        }
    }

    companion object {
        const val ACTION_CLIPBOARD_COPY = "org.monora.uprotocol.client.android.action.CLIPBOARD_COPY"

        const val ACTION_DEVICE_KEY_CHANGE_APPROVAL = "org.monora.uprotocol.client.android.action.DEVICE_APPROVAL"

        const val ACTION_FILE_TRANSFER = "org.monora.uprotocol.client.android.action.FILE_TRANSFER"

        const val ACTION_PIN_USED = "org.monora.uprotocol.client.android.transaction.action.PIN_USED"

        const val ACTION_STOP_ALL_TASKS = "org.monora.uprotocol.client.android.transaction.action.STOP_ALL_TASKS"

        const val EXTRA_SHARED_TEXT = "extraText"

        const val EXTRA_CLIENT = "extraClient"

        const val EXTRA_TRANSFER = "extraTransfer"

        const val EXTRA_ACCEPTED = "extraAccepted"
    }
}
