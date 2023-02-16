package org.monora.uprotocol.client.android.viewholder

import androidx.recyclerview.widget.RecyclerView
import org.monora.uprotocol.client.android.adapter.FileAdapter
import org.monora.uprotocol.client.android.databinding.ListFileNouveauBinding
import org.monora.uprotocol.client.android.model.FileModel
import org.monora.uprotocol.client.android.viewmodel.content.FileContentViewModel

class FileViewHolder(
    private val binding: ListFileNouveauBinding,
    private val clickListener: (FileModel, FileAdapter.ClickType) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(fileModel: FileModel) {
        binding.viewModel = FileContentViewModel(fileModel)
        binding.root.setOnClickListener {
            clickListener(fileModel, FileAdapter.ClickType.Default)
        }
        binding.selection.setOnClickListener {
            fileModel.isSelected = !fileModel.isSelected
            it.isSelected = fileModel.isSelected
            clickListener(fileModel, FileAdapter.ClickType.ToggleSelect)
        }
        binding.selection.isSelected = fileModel.isSelected
        binding.executePendingBindings()
    }
}
