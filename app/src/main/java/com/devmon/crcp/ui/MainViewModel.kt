package com.devmon.crcp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmon.crcp.base.BaseViewModel
import com.devmon.crcp.data.network.response.UserResponse
import com.devmon.crcp.data.network.response.toUi
import com.devmon.crcp.data.repository.StudyRepository
import com.devmon.crcp.domain.model.User
import com.devmon.crcp.domain.repository.MemberRepository
import com.devmon.crcp.ui.model.Study
import com.devmon.crcp.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val studyRepository: StudyRepository,
) : BaseViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _drawerOpenEvent = MutableLiveData<Event<Boolean>>()
    val drawerOpenEvent: LiveData<Event<Boolean>> = _drawerOpenEvent

    private val _goToDrawerMenuView = MutableLiveData<Event<DrawerMenu>>()
    val goToDrawerMenuView: LiveData<Event<DrawerMenu>> = _goToDrawerMenuView

    private val _goToEmptyStudyEvent = MutableLiveData<Event<Unit>>()
    val goToEmptyStudyEvent: LiveData<Event<Unit>> = _goToEmptyStudyEvent

    init {
        fetchUserInfo()
    }

    fun navigateMenu(drawerMenu: DrawerMenu) {
        _drawerOpenEvent.value = Event(false)

        if (drawerMenu == DrawerMenu.LOGOUT) {
            logout()
        }

        if (drawerMenu == DrawerMenu.QNA || drawerMenu == DrawerMenu.SCREENING || drawerMenu == DrawerMenu.CONSENT) {
            getStudy(
                onSuccess = {
                    _goToDrawerMenuView.value = Event(drawerMenu)
                },
                onError = {
                    _goToEmptyStudyEvent.value = Event(Unit)
                }
            )
            return
        }

        _goToDrawerMenuView.value = Event(drawerMenu)
    }

    private fun getStudy(
        onSuccess: (study: Study) -> Unit,
        onError: () -> Unit,
    ) {
        viewModelScope.launch {
            val user = memberRepository.getUserInfo()?.toUi() ?: User.EMPTY
            if (user == User.EMPTY) {
                onError()
            }

            studyRepository.myStudy(user.applid)
                .onFullProgress()
                .collect {
                    if (it.isSuccessful()) {
                        val sid = it.data?.sid ?: 0
                        val said = it.data?.said ?: 0
                        val study = Study(sid, said)
                        onSuccess(study)
                    } else {
                        onError()
                    }
                }
        }
    }

    fun openDrawer() {
        _drawerOpenEvent.value = Event(true)
    }

    private fun fetchUserInfo() {
        viewModelScope.launch {
            val user = getUserInfo() ?: return@launch
            _user.value = user
        }
    }

    suspend fun getUserInfo(): User? = memberRepository.getUserInfo()?.toUi()

    private fun logout() {
        setUserInfo(null)
    }

    fun setUserInfo(userResponse: UserResponse?) {
        memberRepository.setUserInfo(userResponse)
        _user.value = userResponse?.toUi()
    }
}