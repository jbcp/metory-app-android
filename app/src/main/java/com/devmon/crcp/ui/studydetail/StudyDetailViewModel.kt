package com.devmon.crcp.ui.studydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.devmon.crcp.base.BaseViewModel
import com.devmon.crcp.data.network.response.CrcpResponse
import com.devmon.crcp.data.network.response.MyStudyResponse
import com.devmon.crcp.data.repository.StudyRepository
import com.devmon.crcp.domain.model.Alert
import com.devmon.crcp.domain.repository.MemberRepository
import com.devmon.crcp.ui.model.Recruiting
import com.devmon.crcp.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@HiltViewModel
class StudyDetailViewModel @Inject constructor(
    handle: SavedStateHandle,
    private val memberRepository: MemberRepository,
    private val studyRepository: StudyRepository,
) : BaseViewModel() {

    private val _state = MutableLiveData<Event<State>>()
    val state: LiveData<Event<State>> = _state

    private val _study = handle.getLiveData<Recruiting>("recruiting")
    val study: LiveData<Recruiting> = _study

    private val _studyAlert = MutableLiveData<Event<Alert>>()
    val studyAlert: LiveData<Event<Alert>> = _studyAlert

    fun onPrev() {
        _state.value = Event(State.Prev)
    }

    fun onNext() {
        getMyStudy { _, response ->
            if (response.isSuccessful()) {
                if (_study.value?.sid == response.data?.sid) {
                    _studyAlert.value = Event(Alert("알림", "동일 연구에 참여중입니다"))
                } else {
                    _studyAlert.value = Event(Alert("알림", "이미 다른 연구에 참여중입니다"))
                }
            } else {
                _state.value = Event(State.Next(study.value))
            }
        }
    }

    fun getMyStudy(callback: (applid: Int, response: CrcpResponse<MyStudyResponse>) -> Unit) {
        viewModelScope.launch {
            val applid = memberRepository.getUserInfo()?.applid ?: return@launch
            studyRepository.myStudy(applid)
                .onFullProgress()
                .collect {
                    callback.invoke(applid, it)
                }
        }
    }

    fun cancel() {
        getMyStudy { applid, response ->
            if (response.isSuccessful()) {
                viewModelScope.launch {
                    try {
                        val sid = requireNotNull(response.data?.sid) { "sid is null" }
                        val said = requireNotNull(response.data?.said) { "said is null" }
                        val cancel = studyRepository.enrollCancel(sid, applid, said)
                        _alert.value = Event(Alert(content = "${cancel.data}"))
                    } catch (e: Exception) {
                        _alert.value = Event(Alert(content = "${e.message}"))
                    }
                }
            } else {
                _alert.value = Event(Alert(content = "현재 신청한 연구 없음"))
            }
        }
    }

    sealed class State {
        object Prev : State()
        data class Next(val study: Recruiting?) : State()
    }
}