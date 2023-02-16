package org.monora.uprotocol.client.android.viewmodel.content

import com.genonbeta.android.framework.util.Files
import org.monora.uprotocol.client.android.R
import org.monora.uprotocol.client.android.model.FileModel
import org.monora.uprotocol.client.android.util.MimeIcons

class FileContentViewModel(fileModel: FileModel) {
    val name = fileModel.file.getName()

    val count = fileModel.indexCount

    val isDirectory = fileModel.file.isDirectory()

    val mimeType = fileModel.file.getType()

    val icon = if (isDirectory) R.drawable.ic_folder_white_24dp else MimeIcons.loadMimeIcon(mimeType)

    val indexCount = fileModel.indexCount

    val sizeText by lazy {
        Files.formatLength(fileModel.file.getLength(), false)
    }

    val uri = fileModel.file.getUri()
}
