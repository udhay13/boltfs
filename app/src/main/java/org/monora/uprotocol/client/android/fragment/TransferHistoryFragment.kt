package org.monora.uprotocol.client.android.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.monora.uprotocol.client.android.R
import org.monora.uprotocol.client.android.activity.ContentBrowserActivity

@AndroidEntryPoint
class TransferHistoryFragment : Fragment(R.layout.layout_transfer_history) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.sendButton).setOnClickListener {
            startActivity(Intent(it.context, ContentBrowserActivity::class.java))
        }
        view.findViewById<View>(R.id.receiveButton).setOnClickListener {
            findNavController().navigate(TransferHistoryFragmentDirections.actionTransferHistoryFragmentToNavReceive())
        }
    }
}