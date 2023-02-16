package org.monora.uprotocol.client.android.viewmodel.content

import org.monora.uprotocol.client.android.database.model.SharedText

class SharedTextContentViewModel(sharedText: SharedText) {
    val text = sharedText.text

    val dateCreated = sharedText.created
}
