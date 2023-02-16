package org.monora.uprotocol.client.android.model

data class DateSectionContentModel(val dateText: String, val time: Long): ListItem {
    override val listId: Long = dateText.hashCode().toLong() + javaClass.hashCode()
}
