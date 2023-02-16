package org.monora.uprotocol.client.android.viewholder

import androidx.recyclerview.widget.RecyclerView
import org.monora.uprotocol.client.android.database.model.WebTransfer
import org.monora.uprotocol.client.android.databinding.ListWebTransferBinding
import org.monora.uprotocol.client.android.viewmodel.content.WebTransferContentViewModel

class WebTransferViewHolder(
    private val binding: ListWebTransferBinding,
    private val clickListener: (WebTransfer) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(transfer: WebTransfer) {
        binding.viewModel = WebTransferContentViewModel(transfer)
        binding.container.setOnClickListener { clickListener(transfer) }
        binding.executePendingBindings()
    }
}
