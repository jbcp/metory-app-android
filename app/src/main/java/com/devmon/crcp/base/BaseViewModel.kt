package com.devmon.crcp.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmon.crcp.domain.model.Alert
import com.devmon.crcp.utils.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

open class BaseViewModel : ViewModel() {

    protected val _alert = MutableLiveData<Event<Alert>>()
    val alert: LiveData<Event<Alert>> = _alert

    protected val _toast = MutableLiveData<Event<String>>()
    val toast: LiveData<Event<String>> = _toast

    protected val _fullProgress = MutableLiveData<Event<Boolean>>()
    val fullProgress: LiveData<Event<Boolean>> = _fullProgress

    fun <T> Flow<T>.onFullProgress(): Flow<T> {
        return this
            .onStart {
                _fullProgress.value = Event(true)
            }
            .onCompletion {
                _fullProgress.value = Event(false)
            }
    }
}