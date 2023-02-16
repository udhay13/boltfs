package org.monora.uprotocol.client.android.fragment.pickclient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.monora.uprotocol.client.android.databinding.LayoutClientDetailPickBinding
import org.monora.uprotocol.client.android.viewmodel.content.ClientContentViewModel

@AndroidEntryPoint
class AcceptClientFragment : BottomSheetDialogFragment() {
    private val args: AcceptClientFragmentArgs by navArgs()

    private lateinit var binding: LayoutClientDetailPickBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = LayoutClientDetailPickBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = ClientContentViewModel(args.clientRoute.client)
        binding.acceptButton.setOnClickListener {
            findNavController().navigate(
                AcceptClientFragmentDirections.actionAcceptClientFragmentToClientConnectionFragment(
                    args.clientRoute.client, args.clientRoute.address
                )
            )
        }
        binding.rejectButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.executePendingBindings()
    }
}