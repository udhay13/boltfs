package org.monora.uprotocol.client.android.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.monora.uprotocol.client.android.R
import org.monora.uprotocol.client.android.databinding.LayoutTransferDetailsBinding
import org.monora.uprotocol.client.android.service.backgroundservice.Task
import org.monora.uprotocol.client.android.util.Activities
import org.monora.uprotocol.client.android.util.CommonErrors
import org.monora.uprotocol.client.android.viewmodel.TransferDetailsViewModel
import org.monora.uprotocol.client.android.viewmodel.TransferManagerViewModel
import org.monora.uprotocol.client.android.viewmodel.content.ClientContentViewModel
import org.monora.uprotocol.client.android.viewmodel.content.TransferDetailContentViewModel
import javax.inject.Inject

@AndroidEntryPoint
class TransferDetailsFragment : Fragment(R.layout.layout_transfer_details) {
    @Inject
    lateinit var factory: TransferDetailsViewModel.Factory

    private val args: TransferDetailsFragmentArgs by navArgs()

    private val managerViewModel: TransferManagerViewModel by viewModels()

    private val viewModel: TransferDetailsViewModel by viewModels {
        TransferDetailsViewModel.ModelFactory(factory, args.transfer)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = LayoutTransferDetailsBinding.bind(view)

        binding.image.setOnClickListener {
            viewModel.client.value?.let {
                findNavController().navigate(
                    TransferDetailsFragmentDirections.actionTransferDetailsFragmentToClientDetailsFragment2(it)
                )
            }
        }
        binding.showFilesButton.setOnClickListener {
            findNavController().navigate(
                TransferDetailsFragmentDirections.actionTransferDetailsFragmentToTransferItemFragment(args.transfer)
            )
        }
        binding.toggleButton.setOnClickListener {
            val client = viewModel.client.value ?: return@setOnClickListener
            val detail = viewModel.transferDetail.value ?: return@setOnClickListener

            managerViewModel.toggleTransferOperation(args.transfer, client, detail)
        }
        binding.rejectButton.setOnClickListener {
            val client = viewModel.client.value ?: return@setOnClickListener
            managerViewModel.rejectTransferRequest(args.transfer, client)
        }
        binding.openDirectoryButton.setOnClickListener {
            viewModel.runCatching {
                Activities.view(requireActivity(), viewModel.getTransferStorage())
            }
        }

        viewModel.transferDetail.observe(viewLifecycleOwner) {
            if (it == null) {
                findNavController().navigateUp()
            } else {
                binding.transferViewModel = TransferDetailContentViewModel(it).apply {
                    onRemove = viewModel::remove
                }
                binding.executePendingBindings()
            }
        }

        viewModel.client.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.clientViewModel = ClientContentViewModel(it)
                binding.executePendingBindings()
            }
        }

        viewModel.state.observe(viewLifecycleOwner) {
            binding.stateViewModel = it
            binding.executePendingBindings()

            if (it.state is Task.State.Error) context?.let { context ->
                Snackbar.make(
                    binding.root,
                    CommonErrors.messageOf(context, it.state.error),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        viewModel.rejectionState.observe(viewLifecycleOwner) {
            binding.rejectButton.isEnabled = it == null
        }
    }

    companion object {
        const val ACTION_TRANSFER_DETAIL = "org.monora.uprotocol.client.android.action.TRANSFER_DETAIL"
    }
}