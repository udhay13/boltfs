package org.monora.uprotocol.client.android.fragment.pickclient

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionManager
import dagger.hilt.android.AndroidEntryPoint
import org.monora.uprotocol.client.android.NavPickClientDirections
import org.monora.uprotocol.client.android.R
import org.monora.uprotocol.client.android.databinding.LayoutClientConnectionBinding
import org.monora.uprotocol.client.android.util.CommonErrors
import org.monora.uprotocol.client.android.viewmodel.ClientConnectionViewModel
import org.monora.uprotocol.client.android.viewmodel.ClientPickerViewModel
import org.monora.uprotocol.client.android.viewmodel.ConnectionState
import org.monora.uprotocol.client.android.viewmodel.content.ClientContentViewModel

@AndroidEntryPoint
class ClientConnectionFragment : Fragment(R.layout.layout_client_connection) {
    private val args: ClientConnectionFragmentArgs by navArgs()

    private val clientPickerViewModel: ClientPickerViewModel by activityViewModels()

    private val clientConnectionViewModel: ClientConnectionViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = LayoutClientConnectionBinding.bind(view)
        binding.viewModel = ClientContentViewModel(args.client)

        binding.retryButton.setOnClickListener {
            clientConnectionViewModel.start(args.client, args.clientAddress)
        }

        binding.executePendingBindings()

        binding.retryButton.performClick()

        clientConnectionViewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is ConnectionState.Connected -> {
                    clientPickerViewModel.bridge.postValue(it.bridge)
                    findNavController().navigate(NavPickClientDirections.xmlPop())
                }
                is ConnectionState.Error -> {
                    it.e.printStackTrace()
                    binding.textOffline.text = CommonErrors.messageOf(requireContext(), it.e)
                }
                is ConnectionState.Connecting -> {
                }
            }

            val alpha = if (it.isError) 0.5f else 1f

            binding.image.alpha = alpha
            binding.text.alpha = alpha
            binding.textOffline.visibility = if (it.isError) View.VISIBLE else View.GONE
            binding.progress.visibility = if (it.isConnecting) View.VISIBLE else View.GONE
            binding.retryButton.visibility =
                if (!it.isConnecting && it !is ConnectionState.Connected) View.VISIBLE else View.GONE
            TransitionManager.beginDelayedTransition(binding.root as ViewGroup)
        }
    }
}
