package org.monora.uprotocol.client.android.viewholder

import androidx.recyclerview.widget.RecyclerView
import org.monora.uprotocol.client.android.databinding.ListSectionTitleBinding
import org.monora.uprotocol.client.android.model.TitleSectionContentModel
import org.monora.uprotocol.client.android.viewmodel.content.TitleSectionContentViewModel

class TitleSectionViewHolder(val binding: ListSectionTitleBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(contentModel: TitleSectionContentModel) {
        binding.viewModel = TitleSectionContentViewModel(contentModel)
        binding.executePendingBindings()
    }
}