package org.monora.uprotocol.client.android.util

import android.content.Context
import com.genonbeta.android.framework.io.DocumentFile
import org.monora.uprotocol.client.android.database.model.UTransferItem
import org.monora.uprotocol.core.protocol.Direction
import java.io.File

/**
 * created by: veli
 * date: 06.04.2018 17:01
 */
object Transfers {
    fun createStructure(
        context: Context,
        list: MutableList<UTransferItem>,
        progress: Progress,
        groupId: Long,
        contextFile: DocumentFile,
        directory: String? = null,
        progressCallback: (progress: Progress, file: DocumentFile) -> Unit
    ) {
        if (contextFile.isFile()) {
            progress.index += 1
            progressCallback(progress, contextFile)

            val id = progress.index.toLong() // With 'groupId', this will become unique (enough).

            list.add(
                UTransferItem(
                    id,
                    groupId,
                    contextFile.getName(),
                    contextFile.getType(),
                    contextFile.getLength(),
                    directory,
                    contextFile.getUri().toString(),
                    Direction.Outgoing,
                )
            )

            return
        }

        val path = if (directory == null) {
            contextFile.getName()
        } else {
            directory + File.separator + contextFile.getName()
        }
        val files = contextFile.listFiles(context)
        progress.total += files.size
        progressCallback(progress, contextFile)

        for (file in files) {
            createStructure(context, list, progress, groupId, file, path, progressCallback)
        }
    }
}

data class Progress(var total: Int, var index: Int = 0)
