package org.monora.uprotocol.client.android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.monora.uprotocol.client.android.databinding.ListFileNouveauBinding
import org.monora.uprotocol.client.android.databinding.ListSectionTitleBinding
import org.monora.uprotocol.client.android.model.FileModel
import org.monora.uprotocol.client.android.model.ListItem
import org.monora.uprotocol.client.android.model.TitleSectionContentModel
import org.monora.uprotocol.client.android.viewholder.FileViewHolder
import org.monora.uprotocol.client.android.viewholder.TitleSectionViewHolder

class FileAdapter(
    private val clickListener: (FileModel, ClickType) -> Unit
) : ListAdapter<ListItem, ViewHolder>(FilesItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = when (viewType) {
        VIEW_TYPE_FILE -> FileViewHolder(
            ListFileNouveauBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            clickListener
        )
        VIEW_TYPE_SECTION -> TitleSectionViewHolder(
            ListSectionTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
        else -> throw UnsupportedOperationException()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is FileModel -> if (holder is FileViewHolder) holder.bind(item)
            is TitleSectionContentModel -> if (holder is TitleSectionViewHolder) holder.bind(item)
            else -> throw IllegalStateException()
        }
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).listId
    }

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is FileModel -> VIEW_TYPE_FILE
        is TitleSectionContentModel -> VIEW_TYPE_SECTION
        else -> throw IllegalStateException()
    }

    enum class ClickType {
        Default,
        ToggleSelect
    }

    companion object {
        const val VIEW_TYPE_SECTION = 0

        const val VIEW_TYPE_FILE = 1
    }
}

class FilesItemCallback : DiffUtil.ItemCallback<ListItem>() {
    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return oldItem.javaClass == newItem.javaClass && oldItem.listId == newItem.listId
    }

    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return oldItem !is FileModel || newItem !is FileModel || oldItem.file.getLength() == newItem.file.getLength()
                || oldItem.file.getLastModified() == newItem.file.getLastModified()
    }
}
