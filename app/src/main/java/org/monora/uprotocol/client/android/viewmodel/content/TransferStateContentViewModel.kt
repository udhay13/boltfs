package org.monora.uprotocol.client.android.viewmodel.content

import com.genonbeta.android.framework.util.Files
import org.monora.uprotocol.client.android.R
import org.monora.uprotocol.client.android.service.backgroundservice.Task
import org.monora.uprotocol.client.android.task.transfer.TransferParams

class TransferStateContentViewModel(change: Task.Change<TransferParams>?, val total: Int = 0, val progress: Int = 0) {
    val running = change != null && change.state.running

    val buttonIcon = if (running) R.drawable.ic_pause_white_24dp else R.drawable.ic_play_arrow_white_24dp

    val percentageText: String
        get() = (if (total > 0 && progress > 0) ((progress.toDouble() / total) * 100).toInt() else 0).toString()

    val averageSpeedText = if (change != null && running) {
        "${Files.formatLength(change.exported.averageSpeed, true)}/s"
    } else null

    val ongoing = change?.exported?.ongoing?.itemName

    val state = change?.state ?: Task.State.Pending

    companion object {
        fun from(change: Task.Change<TransferParams>?) = when (val state = change?.state) {
            is Task.State.Progress -> TransferStateContentViewModel(change, state.total, state.progress)
            else -> TransferStateContentViewModel(change)
        }
    }
}