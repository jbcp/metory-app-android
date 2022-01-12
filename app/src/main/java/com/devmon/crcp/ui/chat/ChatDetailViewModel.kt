package com.devmon.crcp.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmon.crcp.base.BaseViewModel
import com.devmon.crcp.data.network.request.QnaRequest
import com.devmon.crcp.data.network.response.QnaResponse
import com.devmon.crcp.data.network.response.toUi
import com.devmon.crcp.data.repository.StudyRepository
import com.devmon.crcp.domain.model.User
import com.devmon.crcp.domain.repository.MemberRepository
import com.devmon.crcp.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class ChatDetailViewModel @Inject constructor(
    val studyRepository: StudyRepository,
    val memberRepository: MemberRepository,
) : BaseViewModel() {

    private val tickerChannel = ticker(delayMillis = 30_000, initialDelayMillis = 0)

    val timeDelayList = listOf(1, 2, 3, 4, 5, 10, 20, 30, 60, 120, 300)

    private val _qnaList = MutableLiveData<List<QnaResponse>>()
    val qnaList: LiveData<List<QnaResponse>> = _qnaList

    private val _sendMessage = MutableLiveData<Event<String>>()
    val sendMessage: LiveData<Event<String>> = _sendMessage

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> = _errorMsg

    var sid: Int? = null
    var said: Int? = null

    init {
        viewModelScope.launch {
            val user = memberRepository.getUserInfo()?.toUi() ?: User.EMPTY

            studyRepository.myStudy(user.applid)
                .collect {
                    try {
                        if (it.isSuccessful()) {
                            sid = requireNotNull(it.data?.sid) { "sid is null" }
                            said = requireNotNull(it.data?.said) { "said is null" }
                            observeQnA()
                        } else {
                            throw IllegalStateException(it.error ?: "error")
                        }
                    } catch (e: Exception) {
                        Timber.e(e)
                        showError("참여중인 대화가 없습니다.")
                    }
                }
        }
    }

    private var currentDelay = 0

    private fun observeQnA() {
        viewModelScope.launch {
            if (said != null && sid != null) {
                while (currentDelay < timeDelayList.size) {
                    getQnaList(said!!, sid!!)
                    val i = timeDelayList[currentDelay]
                    delay((i * 1000).toLong())
                }
            }
        }
    }

    private suspend fun getQnaList(said: Int, sid: Int) {
        studyRepository.getQnaList(said, sid)
            .catch { throttle ->
                Timber.e(throttle)
            }.collect {
                if (it.isSuccessful()) {
                    val newList = it.data ?: emptyList()
                    val oldList = _qnaList.value
                    if (oldList != newList) {
                        currentDelay = 0
                        _qnaList.value = newList
                    } else {
                        currentDelay += 1
                    }
                } else {
                    currentDelay += 1
                }
            }
    }

    fun sendMessage(content: String) {
        viewModelScope.launch {
            val user = memberRepository.getUserInfo()?.toUi() ?: User.EMPTY

            studyRepository.sendQna(QnaRequest(user.applid, said!!, content))
                .catch { throttle ->
                    Timber.e(throttle)
                }.collect {
                    it.data?.let {
                        _sendMessage.value = Event(content)
                    }
                }
        }
    }

    private fun showError(msg: String) {
        _isEmpty.value = true
        _errorMsg.value = msg
    }

    override fun onCleared() {
        tickerChannel.cancel()
        super.onCleared()
    }
}