package com.devmon.crcp.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmon.crcp.base.BaseViewModel
import com.devmon.crcp.data.network.request.LoginRequest
import com.devmon.crcp.data.network.response.UserResponse
import com.devmon.crcp.domain.repository.MemberRepository
import com.devmon.crcp.utils.EncryptUtil
import com.devmon.crcp.utils.Event
import com.devmon.crcp.utils.ViewExt
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
) : BaseViewModel() {

    private var _register = MutableLiveData<Event<Unit>>()
    val register: LiveData<Event<Unit>> = _register

    private val _findPassword = MutableLiveData<Event<Unit>>()
    val findPassword: LiveData<Event<Unit>> = _findPassword

    private val _login = MutableLiveData<Event<UserResponse>>()
    val login: LiveData<Event<UserResponse>> = _login

    val email = MutableLiveData("")
    val password = MutableLiveData("")

    fun onRegister() {
        _register.value = Event(Unit)
    }

    fun onFindPassword() {
        _findPassword.value = Event(Unit)
    }

    fun setAutoLogin(isAutoLogin: Boolean) {
        memberRepository.setAutoLogin(isAutoLogin)
    }

    fun getAutoLogin(): Boolean = memberRepository.getAutoLogin()

    fun onLogin() {
        val email = email.value?.trim() ?: ""
        val password = password.value?.trim() ?: ""

        if (email.isEmpty()) {
            _toast.value = Event("이메일을 입력해주세요.")
            return
        }

        if (password.isEmpty()) {
            _toast.value = Event("패스워드를 입력해주세요.")
            return
        }

        if (!ViewExt.isEmailMatch(email)) {
            _toast.value = Event("이메일 형식이 아닙니다.")
            return
        }

        if (!ViewExt.isPasswordMatch(password)) {
//            toast.value = Event("패스워드 형식이 아닙니다.")
//            return
        }

        viewModelScope.launch {
            val pwd = EncryptUtil.sha256(password)
            Timber.e(pwd)
            memberRepository.requestLogin(LoginRequest(email = email, pwd = pwd))
                .onFullProgress()
                .catch { error ->
                    Timber.e(error)
                }.collect {
                    if (it.isSuccessful()) {
                        if (memberRepository.getAutoLogin()) {
                            memberRepository.setUserInfo(it.data!!)
                        }
                        memberRepository.setPassword(pwd)
                        _login.value = Event(it.data!!)
                    } else {
                        _toast.value = Event(it.error!!)
                        Timber.e("login error : ${it.error}")
                    }
                }
        }
    }
}