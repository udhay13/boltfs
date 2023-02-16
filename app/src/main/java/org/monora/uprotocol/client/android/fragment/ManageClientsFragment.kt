package org.monora.uprotocol.client.android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import org.monora.uprotocol.client.android.R
import org.monora.uprotocol.client.android.database.model.UClient
import org.monora.uprotocol.client.android.databinding.LayoutEmptyContentBinding
import org.monora.uprotocol.client.android.databinding.ListManageClientBinding
import org.monora.uprotocol.client.android.itemcallback.UClientItemCallback
import org.monora.uprotocol.client.android.viewmodel.ClientsViewModel
import org.monora.uprotocol.client.android.viewmodel.EmptyContentViewModel
import org.monora.uprotocol.client.android.viewmodel.content.ClientContentViewModel

@AndroidEntryPoint
class ManageClientsFragment : Fragment(R.layout.layout_manage_clients) {
    private val viewModel: ClientsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val emptyView = LayoutEmptyContentBinding.bind(view.findViewById(R.id.emptyView))
        val adapter = Adapter {
            findNavController().navigate(
                ManageClientsFragmentDirections.actionManageDevicesFragment2ToClientDetailsFragment3(it)
            )
        }
        val emptyContentViewModel = EmptyContentViewModel()

        emptyView.viewModel = emptyContentViewModel
        emptyView.emptyText.setText(R.string.empty_clients_list)
        emptyView.emptyImage.setImageResource(R.drawable.ic_devices_white_24dp)
        emptyView.executePendingBindings()
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter

        viewModel.clients.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            emptyContentViewModel.with(recyclerView, it.isNotEmpty())
        }
    }

    class ClientViewHolder(
        val binding: ListManageClientBinding,
        val clickListener: (UClient) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(client: UClient) {
            binding.viewModel = ClientContentViewModel(client)
            binding.clickListener = View.OnClickListener { clickListener(client) }
            binding.detailsClickListener = View.OnClickListener { clickListener(client) }
            binding.executePendingBindings()
        }
    }

    class Adapter(
        private val clickListener: (UClient) -> Unit,
    ) : ListAdapter<UClient, ClientViewHolder>(UClientItemCallback()) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
            return ClientViewHolder(
                ListManageClientBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                clickListener
            )
        }

        override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
            holder.bind(getItem(position))
        }

        override fun getItemId(position: Int): Long {
            return getItem(position).uid.hashCode().toLong()
        }
    }
}
