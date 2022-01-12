package com.devmon.crcp.ui.studydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.devmon.crcp.base.BaseViewModel
import com.devmon.crcp.domain.model.Alert
import com.devmon.crcp.domain.repository.MemberRepository
import com.devmon.crcp.ui.model.Recruiting
import com.devmon.crcp.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@HiltViewModel
class StudyPrivacyPolicyViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val handle: SavedStateHandle,
) : BaseViewModel() {

    private val _state = MutableLiveData<Event<State>>()
    val state: LiveData<Event<State>> = _state

    val policyChecked = MutableLiveData(false)

    private val _terms = MutableLiveData<String>()
    val terms: LiveData<String> = _terms

    init {
        getRegisterTerms()
    }

    private fun getRegisterTerms() {
        viewModelScope.launch {
            memberRepository.getRegisterTerms()
                .onFullProgress()
                .collect {
                    if (it.isSuccessful()) {
                        _terms.value = it.data ?: ""
                    }
                }
        }
    }

    fun onPrev() {
        _state.value = Event(State.Prev)
    }

    fun onNext() {
        if (policyChecked.value != true) {
            _alert.value = Event(Alert("이용동의", "개인정보 이용에 동의해주세요"))
            return
        }
        _state.value = Event(State.Next(handle.get("recruiting")))
    }

    sealed class State {
        object Prev : State()
        data class Next(val study: Recruiting?) : State()
    }
}