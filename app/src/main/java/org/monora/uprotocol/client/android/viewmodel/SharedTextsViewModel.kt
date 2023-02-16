package org.monora.uprotocol.client.android.viewmodel

import android.content.Context
import android.text.format.DateUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import org.monora.uprotocol.client.android.data.SharedTextRepository
import org.monora.uprotocol.client.android.data.UserDataRepository
import org.monora.uprotocol.client.android.database.model.SharedText
import org.monora.uprotocol.client.android.model.DateSectionContentModel
import org.monora.uprotocol.client.android.model.ListItem
import javax.inject.Inject

@HiltViewModel
class SharedTextsViewModel @Inject internal constructor(
    @ApplicationContext context: Context,
    private val userDataRepository: UserDataRepository,
    private val sharedTextRepository: SharedTextRepository,
) : ViewModel() {
    val sharedTexts = Transformations.switchMap(sharedTextRepository.getSharedTexts()) { list ->
        val newList = ArrayList<ListItem>()
        var previous: DateSectionContentModel? = null

        list.forEach {
            val dateText = DateUtils.formatDateTime(context, it.created, DateUtils.FORMAT_SHOW_DATE)
            if (previous?.dateText != dateText) {
                newList.add(DateSectionContentModel(dateText, it.created).also { model -> previous = model })
            }
            newList.add(it)
        }

        MutableLiveData(newList)
    }

    fun save(sharedText: SharedText, update: Boolean) {
        viewModelScope.launch {
            if (update) sharedTextRepository.update(sharedText) else sharedTextRepository.insert(sharedText)
        }
    }

    fun remove(sharedText: SharedText) {
        viewModelScope.launch {
            sharedTextRepository.delete(sharedText)
        }
    }
}
