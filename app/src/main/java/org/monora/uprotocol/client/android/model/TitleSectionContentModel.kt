package org.monora.uprotocol.client.android.model

data class TitleSectionContentModel(val title: String): ListItem {
    override val listId: Long = title.hashCode().toLong() + javaClass.hashCode()
}
