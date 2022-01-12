package com.devmon.crcp.ui.consent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import com.devmon.crcp.base.BaseViewModel
import com.devmon.crcp.domain.repository.MemberRepository
import com.devmon.crcp.ui.model.Consent
import com.devmon.crcp.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class StudyConsentDetailViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val memberRepository: MemberRepository
) : BaseViewModel() {

    val consent = handle.getLiveData<Consent>("consent")

    private val _failed = MutableLiveData<Event<Unit>>()
    val failed: LiveData<Event<Unit>> = _failed


    val enabled = Transformations.map(consent) {
        it.csStage < 5
    }

    val signText = Transformations.map(consent) {
        if (it.csStage < 5) {
            "서명 하기"
        } else {
            "지원자 서명 완료"
        }
    }

    private val _nextEvent = MutableLiveData<Event<Consent>>()
    val nextEvent: LiveData<Event<Consent>> = _nextEvent

    private val _pdfFile = MutableLiveData<Event<File>>()
    val pdfFile: LiveData<Event<File>> = _pdfFile

    fun getLinkUrl(): String {
        return memberRepository.getSiteIp() + consent.value?.fileUrl
    }

    fun onNext() {
        if (consent.value != null) {
            _nextEvent.value = Event(consent.value!!)
        }
    }

    fun savePdfFile(file: File) {
        _pdfFile.value = Event(file)
    }

    fun onDownloadFailed() {
        _failed.value = Event(Unit)
    }

    fun getPdfFile(): File? = pdfFile.value?.peekContent()
}