package com.devmon.crcp.ui.consent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmon.crcp.base.BaseViewModel
import com.devmon.crcp.data.network.response.toUi
import com.devmon.crcp.data.repository.StudyRepository
import com.devmon.crcp.domain.model.Alert
import com.devmon.crcp.domain.model.User
import com.devmon.crcp.domain.repository.MemberRepository
import com.devmon.crcp.ui.model.Consent
import com.devmon.crcp.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
@FlowPreview
class StudyConsentViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val studyRepository: StudyRepository,
) : BaseViewModel() {

    private val _state = MutableLiveData<Event<State>>()
    val state: LiveData<Event<State>> = _state

    private val _consents = MutableLiveData<List<Consent>>()
    val consents: LiveData<List<Consent>> = _consents

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty

    private val _allAgree = MutableLiveData(false)
    val allAgree: LiveData<Boolean> = _allAgree

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> = _errorMsg

    init {
        getMyStudies()
    }

    fun getMyStudies() {
        viewModelScope.launch {
            val user = memberRepository.getUserInfo()?.toUi() ?: User.EMPTY
            if (user == User.EMPTY) {
                // 유저가 비어있음...
                showError("세션이 만료되었습니다. 다시 로그인해주세요.")
                return@launch
            }
            studyRepository.myStudy(user.applid)
                .onFullProgress()
                .collect {
                    try {
                        if (it.isSuccessful()) {
                            val sid = requireNotNull(it.data?.sid) { "sid is null" }
                            val said = requireNotNull(it.data?.said) { "said is null" }
                            getConsentStdgrp(sid, said)
                        } else {
                            throw IllegalStateException(it.error ?: "error")
                        }
                    } catch (e: Exception) {
                        // myStudy 없음...
                        Timber.e(e)
                        showError("참여 중인 연구가 없습니다.")
                    }
                }
        }
    }

    private suspend fun getConsentStdgrp(sid: Int, said: Int) {
        viewModelScope.launch {
            studyRepository.consentStdgrp(sid, said)
                .onFullProgress()
                .collect { response ->
                    if (!response.isSuccessful()) {
                        // allConsent API 요청 실패
                        showError("잠시 후 다시 시도해주세요.")
                        return@collect
                    }

                    hideError()
                    val consents = (response.data ?: emptyList()).map { stdgrp ->
                        Consent(
                            said = said,
                            consentId = stdgrp.consentid,
                            displayName = "${stdgrp.csgrptitle} ${stdgrp.cversion}",
                            fileUrl = stdgrp.cfile ?: "",
                            csClose = stdgrp.csclose ?: 0,
                            csStage = stdgrp.csstage ?: 0,
                            csCloseMsg = if (stdgrp.csclose == 1) "동의" else "미동의",
                            checkContent = stdgrp.check ?: emptyList()
                        )
                    }

                    if (consents.isEmpty()) {
                        // 동의서 비어있음..
                        showError(response.error ?: "입력하신 자원자에 대한 동의 정보가 없습니다.")
                        return@collect
                    }

                    // 전체 동의 완료 상태 여부
                    val isAllAgree =
                        consents.fold(true) { acc, consent -> acc && (consent.csStage == 6) }
                    _allAgree.value = isAllAgree

                    _consents.value = consents
                }
        }
    }

    private fun showError(msg: String) {
        _isEmpty.value = true
        _errorMsg.value = msg
    }

    private fun hideError() {
        _isEmpty.value = false
    }

    fun navigateConsentDetail(consent: Consent) {
        viewModelScope.launch {
            if (consent.csStage > 0) {
                _state.value = Event(State.Detail(consent))
                return@launch
            }

            studyRepository.constStart(consent.said, consent.consentId)
                .onFullProgress()
                .collect {
                    if (it.isSuccessful()) {
                        _state.value = Event(State.Detail(consent))
                    } else {
                        _alert.value = Event(Alert(content = "잠시 후 다시 시도해주세요"))
                    }
                }
        }
    }

    sealed class State {
        data class Detail(val consent: Consent) : State()
    }
}