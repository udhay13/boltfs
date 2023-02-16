package org.monora.uprotocol.client.android.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.monora.uprotocol.client.android.data.ExtrasRepository
import javax.inject.Inject

@HiltViewModel
class LicensesViewModel @Inject internal constructor(
    extrasRepository: ExtrasRepository,
) : ViewModel() {
    val licenses = extrasRepository.getLicenses()
}
