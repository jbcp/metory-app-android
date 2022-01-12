package com.devmon.crcp.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.devmon.crcp.R
import com.devmon.crcp.base.BaseViewModel
import com.devmon.crcp.data.network.request.UserJoinRequest
import com.devmon.crcp.domain.model.Alert
import com.devmon.crcp.domain.model.Gender
import com.devmon.crcp.domain.repository.MemberRepository
import com.devmon.crcp.ui.auth.certification.PassResult
import com.devmon.crcp.utils.EncryptUtil
import com.devmon.crcp.utils.Event
import com.devmon.crcp.utils.ViewExt.isEmailMatch
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.regex.Pattern
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
) : BaseViewModel() {

    private val _finish = MutableLiveData<Event<Unit>>()
    val finish: LiveData<Event<Unit>> = _finish

    val name = MutableLiveData("")
    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val passwordConfirm = MutableLiveData("")
    val phone = MutableLiveData("")
    val birth = MutableLiveData("")
    val gender = MutableLiveData<Gender>()
    val registerAgree = MutableLiveData(false)
    val mobileAgree = MutableLiveData(false)

    private val _terms = MutableLiveData<String>()
    val terms: LiveData<String> = _terms

    private val _termsEvent = MutableLiveData<Event<String>>()
    val termsEvent: LiveData<Event<String>> = _termsEvent

    var passResult: PassResult? = null

    private val _passEvent = MutableLiveData<Event<Unit>>()
    val passEvent: LiveData<Event<Unit>> = _passEvent

    private val _enablePass = MutableLiveData(true)
    val enablePass: LiveData<Boolean> = _enablePass

    private val _passVerifyText = MutableLiveData("미인증")
    val passVerifyText: LiveData<String> = _passVerifyText

    val passVerifyTextColor = Transformations.map(_passVerifyText) {
        if (it == PASS_SUCCESS_TEXT) {
            R.color.color_blue_400
        } else {
            android.R.color.holo_red_dark
        }
    }

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

    fun requestRegister() {
        val map: HashMap<String, String> = hashMapOf(
            "이름" to name.value!!,
            "이메일" to email.value!!,
            "비밀번호" to password.value!!,
            "비밀번호 확인" to passwordConfirm.value!!,
            "핸드폰" to phone.value!!,
            "생년월일" to birth.value!!
        )

        map.forEach {
            if (it.value.isEmpty()) {
                _toast.value = Event("다음 항목이 빈 칸입니다. [${it.key}]")
                return
            }
        }

        if (!isEmailMatch(email.value!!)) {
            _toast.value = Event("이메일 형식이 아닙니다.")
            return
        }

        if (password.value == passwordConfirm.value) {
//            if (!isPasswordMatch(password.value!!)) {
//                _toast.value = Event("비밀번호 형식을 지켜주세요.")
//                return
//            }
        } else {
            _toast.value = Event("비밀번호와 비밀번호 확인이 동일하지 않습니다.")
            return
        }

        if (!Pattern.matches("^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", phone.value)) {
            _toast.value = Event("핸드폰 번호의 형식이 일치하지 않습니다.")
            return
        }

        if (!Pattern.matches(
                "[1-2][0-9]{3}[0-1][0-9][0-3][0-9]",
                birth.value
            )
        ) {
            _toast.value = Event("생년월일 형식이 일치하지 않습니다.")
            return
        }

        if (gender.value == null) {
            _toast.value = Event("성별을 선택해주세요.")
            return
        }

        if (_enablePass.value == true) {
            _toast.value = Event("본인인증을 완료해주세요.")
            return
        }

        if (registerAgree.value != true) {
            _toast.value = Event("회원 가입 동의가 필요합니다.")
            return
        }

        if (mobileAgree.value != true) {
            _toast.value = Event("모바일 앱 사용동의가 필요합니다.")
            return
        }

        val pass = passResult
        if (pass == null) {
            _toast.value = Event("본인인증을 완료해주세요.")
            return
        }

        viewModelScope.launch {
            memberRepository.userJoin(
                UserJoinRequest(
                    name = name.value?.trim() ?: "",
                    email = email.value?.trim() ?: "",
                    pwd = EncryptUtil.sha256(password.value?.trim() ?: ""),
                    birth = addBirthHyphen(),
                    hp = phone.value?.trim() ?: "",
                    sex = gender.value!!.key,
                    passCustomerCode = pass.passCustomerCode,
                    passTxSeqNo = pass.passTxSeqNo,
                    passResultCode = pass.passResultCode,
                    passResultMsg = pass.passResultMsg,
                    passResultName = pass.passResultName,
                    passResultBirthday = pass.passResultBirthday,
                    passResultSexCode = pass.passResultSexCode,
                    passResultLocalForignerCode = pass.passResultLocalForignerCode,
                    passDi = pass.passDi,
                    passCi = pass.passCi,
                    passCiUpdate = pass.passCiUpdate,
                    passTelComCode = pass.passTelComCode,
                    passCellphoneNo = pass.passCellphoneNo,
                    passReturnMsg = pass.passReturnMsg,
                    )
            )
                .onFullProgress()
                .catch { error ->
                    Timber.e(error)
                }.collect {
                    if (it.isSuccessful()) {
                        _toast.value = Event("회원가입이 완료되었습니다.")
                        _finish.value = Event(Unit)
                    } else {
                        _alert.value = Event(Alert("회원가입 실패", "${it.error}"))
                    }
                }
        }
    }

    fun setGender(checkId: Int) {
        gender.value = when (checkId) {
            R.id.rb_male -> Gender.MAN
            else -> Gender.WOMAN
        }
    }

    fun setRegisterAgree(checkId: Int) {
        registerAgree.value = when (checkId) {
            R.id.rb_agree -> true
            else -> false
        }
    }

    fun setMobileAgree(checkId: Int) {
        mobileAgree.value = when (checkId) {
            R.id.rb_mobile_agree -> true
            else -> false
        }
    }

    fun requestPass() {
        _passEvent.value = Event(Unit)
    }

    private fun addBirthHyphen(): String = "${birth.value!!.substring(0, 4)}-${
        birth.value!!.substring(
            4,
            6
        )
    }-${birth.value!!.substring(6, 8)}"

    fun disablePass() {
        _enablePass.value = false
        _passVerifyText.value = PASS_SUCCESS_TEXT
    }

    fun onClickTerms() {
        _termsEvent.value = Event(terms.value ?: "")
    }

    companion object {
        private const val PASS_SUCCESS_TEXT = "인증 완료"
    }
}
