package com.devmon.crcp.ui.study

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmon.crcp.base.BaseViewModel
import com.devmon.crcp.data.repository.StudyRepository
import com.devmon.crcp.domain.repository.MemberRepository
import com.devmon.crcp.ui.model.Recruiting
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@HiltViewModel
class StudyViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val studyRepository: StudyRepository,
) : BaseViewModel() {

    private val _recruitings = MutableLiveData<List<Recruiting>>()
    val recruitings: LiveData<List<Recruiting>> = _recruitings

    fun allRecruiting() {
        viewModelScope.launch {
            studyRepository.allRecruiting()
                .onFullProgress()
                .collect {
                    _recruitings.value = it.data?.map { item ->
                        item.toUi(memberRepository.getSiteIp())
                    }
                }
        }
    }
}