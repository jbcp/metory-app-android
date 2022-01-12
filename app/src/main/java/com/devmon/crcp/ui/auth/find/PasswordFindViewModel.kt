package com.devmon.crcp.ui.auth.find

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmon.crcp.base.BaseViewModel
import com.devmon.crcp.data.network.request.ChangePasswordRequest
import com.devmon.crcp.domain.model.Alert
import com.devmon.crcp.domain.repository.MemberRepository
import com.devmon.crcp.utils.EncryptUtil
import com.devmon.crcp.utils.Event
import com.devmon.crcp.utils.ViewExt
import com.devmon.crcp.utils.ViewExt.isPasswordMatch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class PasswordFindViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
) : BaseViewModel() {

    private val _finish = MutableLiveData<Event<Unit>>()
    val finish: LiveData<Event<Unit>> = _finish

    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val newPassword = MutableLiveData("")
    val newPasswordConfirm = MutableLiveData("")

    fun onSetting() {
        val map = mapOf(
            "이메일" to email.value,
            "기존 비밀번호" to password.value,
            "새 비밀번호" to newPassword.value,
            "새 비밀번호 확인" to newPasswordConfirm.value
        )

        map.forEach {
            if (it.value!!.isEmpty()) {
                _toast.value = Event("다음 항목이 빈 칸입니다. [${it.key}]")
                return
            }
        }

        if (!ViewExt.isEmailMatch(email.value!!)) {
            _toast.value = Event("이메일 형식이 아닙니다.")
            return
        }

        if (!isPasswordMatch(password.value!!)) {
            _toast.value = Event("비밀번호 형식을 지켜주세요.")
            return
        }

        if (newPassword.value == newPasswordConfirm.value) {
            if (!isPasswordMatch(newPassword.value!!)) {
                _toast.value = Event("새 비밀번호 형식을 지켜주세요.")
                return
            }
        } else {
            _toast.value = Event("새 비밀번호와 새 비밀번호 확인이 동일하지 않습니다.")
            return
        }

        viewModelScope.launch {
            memberRepository.changePassword(
                ChangePasswordRequest(
                    email = email.value!!,
                    pwd = EncryptUtil.sha256(password.value!!),
                    newpwd = EncryptUtil.sha256(newPassword.value!!),
                    newpwd2 = EncryptUtil.sha256(newPasswordConfirm.value!!)
                )
            )
                .onFullProgress()
                .catch { error ->
                    Timber.e(error)
                }.collect {
                    if (it.code == 200) {
                        _toast.value = Event("비밀번호 변경이 완료되었습니다.")
                        _finish.value = Event(Unit)
                    } else {
                        _alert.value = Event(Alert("비밀번호 변경 실패", "${it.output}"))
                    }
                }
        }
    }
}