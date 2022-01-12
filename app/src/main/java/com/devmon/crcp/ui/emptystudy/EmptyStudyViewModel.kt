package com.devmon.crcp.ui.emptystudy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmon.crcp.base.BaseViewModel
import com.devmon.crcp.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EmptyStudyViewModel @Inject constructor() : BaseViewModel() {

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _goStudyListEvent = MutableLiveData<Event<Unit>>()
    val goStudyListEvent: LiveData<Event<Unit>> = _goStudyListEvent

    fun setErrorMessage(message: String) {
        _message.value = message
    }

    fun onClickStudyList() {
        _goStudyListEvent.value = Event(Unit)
    }
}