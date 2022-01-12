package com.devmon.crcp.ui.consent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.devmon.crcp.base.BaseViewModel
import com.devmon.crcp.data.network.response.Check
import com.devmon.crcp.ui.model.Consent
import com.devmon.crcp.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StudyConsentCheckViewModel @Inject constructor(
    private val handle: SavedStateHandle
) : BaseViewModel() {

    val consent = handle.getLiveData<Consent>("consent")

    private val _next = MutableLiveData<Event<Unit>>()
    val next: LiveData<Event<Unit>> = _next

    fun getCheckList(): List<Check> {
        val list = consent.value?.checkContent ?: emptyList()
        return list.map {
            it.copy(accept = false)
        }
    }

    fun onNext() {
        _next.value = Event(Unit)
    }
}