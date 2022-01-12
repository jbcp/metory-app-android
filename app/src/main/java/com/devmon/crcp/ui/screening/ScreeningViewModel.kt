package com.devmon.crcp.ui.screening

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmon.crcp.Constants
import com.devmon.crcp.base.BaseViewModel
import com.devmon.crcp.data.mapper.filterAnswerItemListByConcept
import com.devmon.crcp.data.mapper.toDomain
import com.devmon.crcp.data.network.request.AnswerRequest
import com.devmon.crcp.data.network.request.DrugRequest
import com.devmon.crcp.data.network.request.ScreeningRequest
import com.devmon.crcp.data.network.response.CrcpResponse
import com.devmon.crcp.data.network.response.SurveyLoadResponse
import com.devmon.crcp.data.repository.StudyRepository
import com.devmon.crcp.domain.model.Alert
import com.devmon.crcp.domain.model.AnswerItem
import com.devmon.crcp.domain.model.Concept
import com.devmon.crcp.domain.model.DailyDrugAnswer
import com.devmon.crcp.domain.model.Drug
import com.devmon.crcp.domain.model.DrugFactory
import com.devmon.crcp.domain.model.Gender
import com.devmon.crcp.domain.model.Question
import com.devmon.crcp.domain.model.Screening
import com.devmon.crcp.domain.model.SurveySection
import com.devmon.crcp.domain.repository.MemberRepository
import com.devmon.crcp.domain.repository.ScreeningRepository
import com.devmon.crcp.ui.model.Clock
import com.devmon.crcp.utils.Event
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@HiltViewModel
class ScreeningViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val studyRepository: StudyRepository,
    private val screeningRepository: ScreeningRepository,
    private val clock: Clock,
) : BaseViewModel() {

    private val drugFactory = DrugFactory()

    private var screening: Screening? = null
    private var said: Int = 0

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _birth = MutableLiveData<String>()
    val birth: LiveData<String> = _birth

    private val _gender = MutableLiveData<Gender>()
    val gender: LiveData<Gender> = _gender

    private val _sectionList = MutableLiveData<List<SurveySection>>()
    val sectionList: LiveData<List<SurveySection>> = _sectionList

    private val _drugList = MutableLiveData<List<Drug>>()
    val drugList: LiveData<List<Drug>> = _drugList

    private var currentPage = 1

    private val _topProgress = MutableLiveData<Int>()
    val topProgress: LiveData<Int> = _topProgress

    private val _goToHomeEvent = MutableLiveData<Event<Unit>>()
    val goToHomeEvent: LiveData<Event<Unit>> = _goToHomeEvent

    private val _enabledUpdate = MutableLiveData(true)
    val enabledUpdate: LiveData<Boolean> = _enabledUpdate

    private val _state = MutableLiveData<UiState>(UiState.loading())
    val state: LiveData<UiState> = _state

    private val _progressVisibility = MutableLiveData<Boolean>()
    val progressVisibility: LiveData<Boolean> = _progressVisibility

    init {
        loadSurvey()
    }

    @FlowPreview
    fun loadSurvey() {
        viewModelScope.launch {
            val userInfo = memberRepository.getUserInfo() ?: return@launch
            val id = userInfo.applid ?: return@launch
            _name.value = userInfo.applname ?: ""
            _birth.value = userInfo.applbrthdtc ?: ""
            _gender.value = when (userInfo.applsex) {
                2 -> Gender.WOMAN
                else -> Gender.MAN
            }

            studyRepository.myStudy(id)
                .flatMapConcat {
                    if (it.isSuccessful() && it.data != null) {
                        said = it.data.said ?: return@flatMapConcat flowOf(CrcpResponse.error(
                            Constants.NOT_PARTICIPATE_STUDY))
                        val sid = it.data.sid ?: return@flatMapConcat flowOf(CrcpResponse.error(
                            Constants.NOT_PARTICIPATE_STUDY))
                        screeningRepository.getScreening(sid)
                    } else {
                        flowOf(CrcpResponse.error(Constants.NOT_PARTICIPATE_STUDY))
                    }
                }
                .flatMapConcat {
                    if (it.isSuccessful() && it.data != null) {
                        screening = it.data.toDomain()
                        screeningRepository.load(said)
                    } else {
                        if (it.isError()) {
                            flowOf(CrcpResponse.error(it.error))
                        } else {
                            flowOf(CrcpResponse.error(Constants.EMPTY_SCREENING))
                        }
                    }
                }
                .collectLatest {
                    if (it.isSuccessful() && it.data != null) {
                        _state.value = UiState.success()
                        fetchSectionData(it.data)
                    } else {
                        _state.value = UiState.error(it.error)
                    }
                }
        }
    }

    private fun fetchSectionData(response: SurveyLoadResponse) {
        val sectionList = screening?.sectionList ?: emptyList()
        val answerList = screening?.answerItemList ?: emptyList()
        val subQuestionList = screening?.subQuestionList ?: emptyList()
        drugFactory.replaceAll(subQuestionList)

        val cacheMap = hashMapOf<Int, String>()

        (response.screeningAnswer ?: emptyList()).forEach {
            if (it.screeningQuestionId != null) {
                cacheMap[it.screeningQuestionId] = it.answerSourceValue ?: ""
            }
        }

        _enabledUpdate.value = cacheMap.isEmpty()

        val fetchedSectionList = sectionList.map { section ->
            val fetchedList = section.questionList.map question@{ question ->
                val text = cacheMap[question.screeningQuestionId] ?: return@question question
                val filteredList = filterAnswerItemListByConcept(question.answerItemGroupId,
                    answerList,
                    question.answerConcept)
                val answer = filteredList.find { it.text == text } ?: AnswerItem.text(text)

                question.copy(selectedAnswer = answer)
            }
            section.copy(questionList = fetchedList)
        }

        val drugList = mutableListOf<Drug>()
        val subQuestionMap = (response.groupAnswer ?: emptyList())
            .groupBy { it.subQuestionId ?: -1 }
            .filter { it.key != -1 }

        val size = subQuestionMap.values.minOfOrNull { it.size } ?: 0

        for (i in 0 until size) {
            val list = mutableListOf<SurveyLoadResponse.GroupAnswerResponse>()

            for (j in subQuestionMap.keys.indices) {
                val q = subQuestionMap.values.toList()[j][i]
                list.add(q)
            }
            val drug = drugFactory.createDrug(drugList.size + 1)

            val drugCacheMap = hashMapOf<Int, String>()

            list.forEach {
                if (it.subQuestionId != null) {
                    drugCacheMap[it.subQuestionId] = it.answerSourceValue ?: ""
                }
            }

            val updatedQuestionList = drug.questionList.map question@{ question ->
                val text = drugCacheMap[question.id] ?: return@question question
                val answer = answerList.find { it.text == text }
                    ?: AnswerItem.text(
                        if (question.id == 2) {
                            toJson(text)
                        } else {
                            text
                        }
                    )

                question.copy(selectedAnswer = answer)
            }

            drugList.add(drug.copy(questionList = updatedQuestionList))
        }

        if (drugList.isEmpty()) {
            onAddDrugClick()
        }

        _sectionList.value = fetchedSectionList
        setCurrentPage(currentPage)
        _progressVisibility.value = (_sectionList.value?.size ?: 0) > 1

        _drugList.value = drugList
    }

    fun setCurrentPage(page: Int) {
        currentPage = page
        val progress = page / (_sectionList.value?.size ?: 1).toFloat() * 100
        _topProgress.value = progress.toInt()
    }

    fun getNextPage(): Int {
        if (isLastPage(currentPage)) {
            return -1
        }
        return currentPage + 1
    }

    fun isLastPage(id: Int) = id == (_sectionList.value?.lastOrNull()?.id ?: 1)

    fun saveQuestionAnswer(sectionId: Int, questionId: Int, answer: AnswerItem) {
        val list = (_sectionList.value ?: emptyList()).toMutableList()

        val sectionIndex = list.indexOfFirst { it.id == sectionId }
        if (sectionIndex == -1) return

        val section = list[sectionIndex]
        val questionIndex = section.questionList.indexOfFirst { it.id == questionId }

        if (questionIndex == -1) return

        val questionList = section.questionList.toMutableList()

        val question = questionList[questionIndex]
        questionList[questionIndex] = question.copy(selectedAnswer = answer)

        list[sectionIndex] = section.copy(questionList = questionList)

        _sectionList.value = list
    }

    fun onAddDrugClick() {
        val list = (_drugList.value ?: emptyList()).toMutableList()
        list.add(
            drugFactory.createDrug(list.size + 1)
        )
        _drugList.value = list
    }

    fun onDrugRemove(drug: Drug) {
        val list = (_drugList.value ?: emptyList()).toMutableList()
        list.remove(drug)

        val sortedList = list.mapIndexed { i, drugItem ->
            drugItem.copy(id = i + 1)
        }

        _drugList.value = sortedList
    }

    fun onDrugChange(drugId: Int, questionId: Int, answer: AnswerItem) {
        val list = (_drugList.value ?: emptyList()).toMutableList()

        val updatedList = list.map { drug ->
            if (drug.id != drugId) return@map drug

            val questions = drug.questionList.map question@{
                if (it.id == questionId) {
                    return@question it.copy(selectedAnswer = answer)
                }
                it
            }
            drug.copy(questionList = questions)
        }

        _drugList.value = updatedList
    }

    fun updateSurvey() {
        viewModelScope.launch {
            val userInfo = memberRepository.getUserInfo() ?: return@launch

            val answerList = (_sectionList.value ?: emptyList())
                .flatMap {
                    it.questionList.filter { question -> question.answerConcept != Concept.INFO }
                }
                .filter {
                    // gender 에 따라서 여자는 15, 16, 남자는 17, 18 을 추가함
                    if (_gender.value == Gender.WOMAN) {
                        it.screeningQuestionId != 17 || it.screeningQuestionId != 18
                    } else {
                        it.screeningQuestionId != 15 || it.screeningQuestionId != 16
                    }
                }
                .map {
                    AnswerRequest(id = it.screeningQuestionId, body = it.selectedAnswer.text)
                }

            val subList = mutableListOf<DrugRequest.ResponseSubAnswer>()

            if (isCheckedDrug()) {
                val list = (_drugList.value ?: emptyList())
                    .flatMap { drug ->
                        drug.questionList.map {
                            drug.id to it
                        }
                    }
                    .map { (drugId, question) ->
                        val answer = if (question.id == 2) {
                            toRequest(question.selectedAnswer.text)
                        } else {
                            question.selectedAnswer.text
                        }

                        DrugRequest.ResponseSubAnswer(
                            answer = answer,
                            questionGroupId = 1,
                            subQuestionId = question.id,
                        )
                    }
                subList.addAll(list)
            }

            val request = ScreeningRequest(
                said = said.toString(),
                applid = userInfo.applid.toString(),
                screeningId = "1",
                datetime = clock.now(),
                list = answerList,
                subList = subList,
            )

            screeningRepository.save(request)
                .onFullProgress()
                .collectLatest {
                    if (it.isSuccessful()) {
                        _toast.value = Event("저장되었습니다.")
                        _goToHomeEvent.value = Event(Unit)
                    } else {
                        _alert.value = Event(Alert("저장 실패", "잠시 후 다시 시도해주세요."))
                    }
                }
        }
    }

    private fun toRequest(jsonAnswer: String): String {
        val dailyDrugAnswer = Gson().fromJson(jsonAnswer, DailyDrugAnswer::class.java)

        return dailyDrugAnswer.run {
            "1일 ${dayCount}회 1회당 ${drugCapacity}${drugType}"
        }
    }

    private fun toJson(response: String): String {
        val split = response.split(" ")

        val dayCount = split.getOrNull(1)?.replace("[^0-9]".toRegex(), "")?.toIntOrNull() ?: 1
        val drugCapacity =
            split.getOrNull(3)?.replace("[^0-9]".toRegex(), "")?.toIntOrNull()?.toString() ?: ""
        val drugType =
            split.getOrNull(3)?.replace("[^ㄱ-ㅎ가-힣a-zA-Z]".toRegex(), "")?.toIntOrNull()?.toString()
                ?: "알"

        val dailyDrugAnswer = DailyDrugAnswer(dayCount, drugCapacity, drugType)

        return Gson().toJson(dailyDrugAnswer)
    }

    fun findNoneAnswerList(sectionId: Int): List<String> {
        if (enabledUpdate.value != true) return emptyList()

        val list = mutableListOf<String>()

        val sectionList = (_sectionList.value ?: emptyList()).toMutableList()

        val section = sectionList.firstOrNull { it.id == sectionId } ?: return list

        section.questionList
            .filter { it.answerConcept != Concept.INFO }
            .filter(Question::required)
            .forEach { question ->
                if (question.selectedAnswer == AnswerItem.NONE) {
                    list.add(
                        "${question.content}\n\n항목 입력이 누락되었습니다. 계속하시겠습니까?"
                    )
                }
            }

        return list
    }

    fun validateDrugList(): Pair<Boolean, String> {
        if (enabledUpdate.value != true) return true to ""

        val sectionList = (_sectionList.value ?: emptyList()).toMutableList()

        val section = sectionList.firstOrNull { it.id == 3 } ?: return true to ""

        // 1번 답이 예 라는 것을 알고 있다.
        // 1번 질문이 약물을 추가하는 것인지도 알고 있다.
        // 나만 알고 있다.
        if (section.questionList[0].selectedAnswer.seq != 1) {
            return true to ""
        }

        (_drugList.value ?: emptyList()).forEach { drug ->
            drug.questionList
                .filter { it.answerConcept != Concept.INFO }
                .filter(Question::required)
                .forEach {
                    if (it.selectedAnswer == AnswerItem.NONE) {
                        return false to "'약물 ${drug.id}' 항목을 모두 채워주세요."
                    }
                }
        }

        return true to ""
    }

    private fun isCheckedDrug(): Boolean {
        val sectionList = (_sectionList.value ?: emptyList()).toMutableList()

        val section = sectionList.firstOrNull { it.id == 3 } ?: return false

        // 1번 답이 예 라는 것을 알고 있다.
        // 1번 질문이 약물을 추가하는 것인지도 알고 있다.
        // 나만 알고 있다.
        if (section.questionList[0].selectedAnswer.seq != 1) {
            return false
        }

        return true
    }

    fun changeName(text: String) {
        _name.value = text
    }

    fun changeBirth(date: String) {
        _birth.value = date
    }

    fun changeGender(gender: Int) {
        _gender.value = when (gender) {
            2 -> Gender.WOMAN
            else -> Gender.MAN
        }
    }

    fun goToHome() {
        _goToHomeEvent.value = Event(Unit)
    }
}