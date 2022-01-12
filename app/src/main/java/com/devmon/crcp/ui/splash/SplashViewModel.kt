package com.devmon.crcp.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmon.crcp.base.BaseViewModel
import com.devmon.crcp.data.network.request.LoginRequest
import com.devmon.crcp.domain.repository.MemberRepository
import com.devmon.crcp.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class SplashViewModel @Inject constructor(private val memberRepository: MemberRepository) :
    BaseViewModel() {

    private val _loginEvent = MutableLiveData<Event<Unit>>()
    val loginEvent: LiveData<Event<Unit>> = _loginEvent

    private val _homeEvent = MutableLiveData<Event<Unit>>()
    val homeEvent: LiveData<Event<Unit>> = _homeEvent

    fun navigate() {
        viewModelScope.launch {
            delay(2000L)

            memberRepository.getApiInfo()
                .onFullProgress()
                .collect {
                    if (it.isSuccessful()) {
                        memberRepository.saveSiteIp(it.data?.siteIp ?: "")
                        memberRepository.saveSubjectIp(it.data?.subjectIp ?: "")
                    }
                }

            if (!memberRepository.getAutoLogin()) {
                _loginEvent.value = Event(Unit)
                return@launch
            }

            val userInfo = memberRepository.getUserInfo() ?: run {
                _loginEvent.value = Event(Unit)
                return@launch
            }

            val password = memberRepository.getPassword()

            memberRepository.requestLogin(LoginRequest(
                userInfo.applmail ?: "",
                password
            ))
                .onFullProgress()
                .collectLatest {
                    if (it.isSuccessful()) {
                        memberRepository.setUserInfo(it.data)
                        _homeEvent.value = Event(Unit)
                    } else {
                        _loginEvent.value = Event(Unit)
                    }
                }
        }
    }
}