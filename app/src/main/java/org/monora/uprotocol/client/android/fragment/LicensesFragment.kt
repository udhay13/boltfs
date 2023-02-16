package org.monora.uprotocol.client.android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.monora.uprotocol.client.android.R
import org.monora.uprotocol.client.android.databinding.ListLibraryLicenseBinding
import org.monora.uprotocol.client.android.model.LibraryLicense
import org.monora.uprotocol.client.android.viewholder.LibraryLicenseViewHolder
import org.monora.uprotocol.client.android.viewmodel.LicensesViewModel

/**
 * created by: veli
 * date: 7/20/18 8:56 PM
 */
@AndroidEntryPoint
class LicensesFragment : BottomSheetDialogFragment() {
    private val licensesViewModel: LicensesViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_licenses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = LicensesAdapter()

        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter

        licensesViewModel.licenses.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    class LicensesAdapter : ListAdapter<LibraryLicense, LibraryLicenseViewHolder>(LibraryLicenseItemCallback()) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryLicenseViewHolder {
            return LibraryLicenseViewHolder(
                ListLibraryLicenseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }

        override fun onBindViewHolder(holder: LibraryLicenseViewHolder, position: Int) {
            holder.bind(getItem(position))
        }

        override fun getItemId(position: Int): Long {
            return getItem(position).hashCode().toLong()
        }
    }
}

class LibraryLicenseItemCallback : DiffUtil.ItemCallback<LibraryLicense>() {
    override fun areItemsTheSame(oldItem: LibraryLicense, newItem: LibraryLicense): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: LibraryLicense, newItem: LibraryLicense): Boolean {
        return oldItem == newItem
    }
}
