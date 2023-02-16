package org.monora.uprotocol.client.android.viewmodel.content

import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.genonbeta.android.framework.util.Files
import org.monora.uprotocol.client.android.R
import org.monora.uprotocol.client.android.database.model.TransferDetail
import org.monora.uprotocol.client.android.protocol.isIncoming
import kotlin.math.max

class TransferDetailContentViewModel(detail: TransferDetail) {
    val clientNickname = detail.clientNickname

    val sizeText = Files.formatLength(detail.size, false)

    val isFinished = detail.itemsCount == detail.itemsDoneCount

    val isReceiving = detail.direction.isIncoming

    val count = detail.itemsCount

    val icon = if (isReceiving) {
        R.drawable.ic_arrow_down_white_24dp
    } else {
        R.drawable.ic_arrow_up_white_24dp
    }

    val finishedIcon = R.drawable.ic_done_white_24dp

    val needsApproval = !detail.accepted && isReceiving

    var onRemove: (() -> Unit)? = null

    private val percentage = with(detail) {
        if (sizeOfDone <= 0) 0 else ((sizeOfDone.toDouble() / size) * 100).toInt()
    }

    val progress = max(1, percentage)

    val percentageText = percentage.toString()

    val waitingApproval = !detail.accepted && !isReceiving

    fun showPopupMenu(view: View) {
        PopupMenu(view.context, view).apply {
            inflate(R.menu.transfer_details)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.remove -> onRemove?.invoke()
                    else -> return@setOnMenuItemClickListener false
                }

                true
            }
            show()
        }
    }
}
