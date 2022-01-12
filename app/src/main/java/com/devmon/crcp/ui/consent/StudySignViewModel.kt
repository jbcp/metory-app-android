package com.devmon.crcp.ui.consent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.devmon.crcp.base.BaseViewModel
import com.devmon.crcp.data.network.response.toUi
import com.devmon.crcp.data.repository.StudyRepository
import com.devmon.crcp.domain.model.Alert
import com.devmon.crcp.domain.model.User
import com.devmon.crcp.domain.repository.MemberRepository
import com.devmon.crcp.ui.model.Clock
import com.devmon.crcp.ui.model.Consent
import com.devmon.crcp.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class StudySignViewModel @Inject constructor(
    private val clock: Clock,
    private val memberRepository: MemberRepository,
    private val studyRepository: StudyRepository,
    private val handle: SavedStateHandle,
) : BaseViewModel() {

    val consent = handle.get<Consent>("consent")

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    val sign = MutableLiveData<String>()

    private val _state = MutableLiveData<Event<State>>()
    val state: LiveData<Event<State>> = _state

    init {
        viewModelScope.launch {
            val user = memberRepository.getUserInfo()?.toUi() ?: User.EMPTY
            _name.value = user.applname
        }
    }

    fun onNext() {
        val name = name.value?.trim() ?: ""
        val sign = sign.value?.trim() ?: ""

        viewModelScope.launch {
            consent?.let {
                Timber.e(it.toString())
                Timber.e(name)
                Timber.e(sign)
                Timber.e(clock.now())
                studyRepository.constAppl(it.said, it.consentId, name, name, clock.now())
                    .onFullProgress()
                    .collect { response ->
                        if (response.isSuccessful()) {
                            _toast.value = Event("참여 신청이 완료되었습니다.")
                            _state.value = Event(State.Next)
                        } else {
                            _alert.value = Event(Alert("저장 실패", response.error ?: "잠시 후 다시 시도해주세요."))
                        }
                    }
            }
        }
    }

    fun onPrev() {
        _state.value = Event(State.Prev)
    }

    sealed class State {
        object Prev : State()
        object Next : State()
    }
}