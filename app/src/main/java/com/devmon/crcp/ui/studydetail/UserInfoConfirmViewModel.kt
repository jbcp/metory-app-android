package com.devmon.crcp.ui.studydetail

import androidx.lifecycle.*
import com.devmon.crcp.base.BaseViewModel
import com.devmon.crcp.data.network.response.toUi
import com.devmon.crcp.data.repository.StudyRepository
import com.devmon.crcp.domain.model.Alert
import com.devmon.crcp.domain.model.User
import com.devmon.crcp.domain.repository.MemberRepository
import com.devmon.crcp.ui.auth.certification.PassResult
import com.devmon.crcp.ui.model.Recruiting
import com.devmon.crcp.ui.screening.formattedString
import com.devmon.crcp.utils.Event
import com.devmon.crcp.utils.PhoneNumberFormatter
import com.devmon.crcp.utils.sexToString
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import timber.log.Timber

@HiltViewModel
class UserInfoConfirmViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val memberRepository: MemberRepository,
    private val studyRepository: StudyRepository,
) : BaseViewModel() {

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _state = MutableLiveData<Event<State>>()
    val state: LiveData<Event<State>> = _state

    private val _birth = MutableLiveData<String>()
    val birth: LiveData<String> = _birth

    private val _phone = MutableLiveData<String>()
    val phone: LiveData<String> = _phone

    private val _sex = MutableLiveData<String>()
    val sex: LiveData<String> = _sex

    private val _cellphone = MutableLiveData<Event<Unit>>()
    val cellphone: LiveData<Event<Unit>> = _cellphone

    private val _genderEdit = MutableLiveData<Event<Int>>()
    val genderEdit: LiveData<Event<Int>> = _genderEdit

    private val _birthEdit = MutableLiveData<Event<LocalDate>>()
    val birthEdit: LiveData<Event<LocalDate>> = _birthEdit

    private val _gender = MutableLiveData<Int>()
    val genderText: LiveData<String> = Transformations.map(_gender) {
        if (it == 1) "남" else "여"
    }

    init {
        getInfo()
    }

    private fun getInfo() {
        viewModelScope.launch {
            val user = memberRepository.getUserInfo()?.toUi()
            if (user != null) {
                _name.value = user.applname
                _phone.value = user.applphonenum
                _birth.value = user.applbrthdtc
                _gender.value = user.applsex
                _email.value = user.applmail
            }
        }
    }

    fun detail() = viewModelScope.launch {
        val user = memberRepository.getUserInfo()?.toUi() ?: User.EMPTY
        Timber.e(user.toString())
        if (user != User.EMPTY) {
            _birth.value = user.applbrthdtc
            _phone.value = user.applphonenum
            _sex.value = sexToString(user.applsex)
        }
    }


    fun onPrev() {
        _state.value = Event(State.Prev)
    }

    fun onNext() {
        enrollApply()
    }

    private fun enrollApply() = viewModelScope.launch {
        val study = handle.get<Recruiting>("recruiting") ?: return@launch
        val sid = study.sid ?: return@launch
        val applid = memberRepository.getUserInfo()?.applid ?: return@launch
        val resp = studyRepository.enrollApply(sid, applid)
        if (resp.isNotEmpty()) {
            _state.value = Event(State.Next)
        } else {
            _alert.value = Event(Alert("연구 등록 실패", "잠시 후 다시 시도해주세요."))
        }
    }

    fun onGenderEdit() {
        _genderEdit.value = Event(_gender.value!!)
    }

    fun onPhone() {
        _cellphone.value = Event(Unit)
    }

    fun onBirthEdit() {
        try {
            val birth = _birth.value!!
            val date = birth.split("-").map { it.toInt() }
            val localDate = LocalDate.of(date[0], date[1], date[2])
            _birthEdit.value = Event(localDate)
        } catch (e: Exception) {
            Timber.w(e)
            _alert.value = Event(Alert("알림", "잠시 후 다시 시도해주세요."))
        }
    }

    fun handlePassResult(result: PassResult) {
        // todo: 핸드폰번호 양식에 맞나 체크해야 함
        Timber.d("pass result phone: ${result.passCellphoneNo}")
        _phone.value = result.passCellphoneNo
    }

    fun updateInfo() {
        viewModelScope.launch {
            val password = memberRepository.getPassword()

            memberRepository.updateUserInfo(
                applid = _name.value!!,
                applpwd = password,
                applsex = _gender.value!!,
                applphonenum = PhoneNumberFormatter.translatePhoneHyphen(_phone.value!!),
                applbrthdtc = _birth.value!!
            )
                .onFullProgress()
                .catch { throttle ->
                    Timber.tag("test").e(throttle)
                }.collectLatest {
                    if (it.isSuccessful()) {
                        detail()
                    } else {
                        _alert.value = Event(Alert("변경 실패", "잠시 후 다시 시도해주세요."))
                    }
                }
        }
    }

    fun setDate(date: LocalDate) {
        _birth.value = date.formattedString()
    }

    fun setGender(gender: Int) {
        _gender.value = gender
    }

    sealed class State {
        object Prev : State()
        object Next : State()
    }
}
