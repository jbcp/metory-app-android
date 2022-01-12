package com.devmon.crcp.ui.screening.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devmon.crcp.domain.model.AnswerItem
import com.devmon.crcp.domain.model.Concept
import com.devmon.crcp.domain.model.Question
import com.devmon.crcp.ui.screening.ScreeningViewModel
import com.devmon.crcp.ui.screening.UiState
import com.google.android.material.composethemeadapter.MdcTheme

@Composable
fun SurveyScreen(
    viewModel: ScreeningViewModel,
    sectionId: Int,
    onNextButtonClick: () -> Unit,
    onErrorClick: () -> Unit,
) {
    val enabled by viewModel.enabledUpdate.observeAsState(true)

    val state = viewModel.sectionList.observeAsState(emptyList())
    val drugList by viewModel.drugList.observeAsState(emptyList())
    val gender by viewModel.gender.observeAsState(1)

    val uiState by viewModel.state.observeAsState()

    if (uiState == UiState.Loading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CircularProgressIndicator(modifier = Modifier.size(56.dp))
        }
        return
    } else if (uiState is UiState.Error) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = (uiState as UiState.Error).message(),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.size(24.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(8.dp),
                onClick = onErrorClick,
            ) {
                Text(text = "연구 목록으로 가기")
            }
        }
        return
    }

    val section = state.value.find { it.id == sectionId } ?: return

    Column {
        SubTitle(section.name)

        if (section.id == 1) {
            SurveyInfo(viewModel)
        }

        section.questionList.forEach { question ->
            if (gender == 1) {
                if (question.screeningQuestionId == 15 || question.screeningQuestionId == 16) {
                    return@forEach
                }
            } else {
                if (question.screeningQuestionId == 17 || question.screeningQuestionId == 18) {
                    return@forEach
                }
            }

            when (question.answerConcept) {
                Concept.RADIO -> {
                    when (question.answerItemGroupId) {
                        1 -> RadioQuestion(question, enabled) { answer ->
                            viewModel.saveQuestionAnswer(section.id, question.id, answer)
                        }
                        3 -> SelectionQuestion(question, enabled) { answer ->
                            viewModel.saveQuestionAnswer(section.id, question.id, answer)
                        }
                        else -> Unit
                    }
                }
                Concept.TEXT -> Unit
                Concept.SELECTION -> SelectionQuestion(question, enabled) { answer ->
                    viewModel.saveQuestionAnswer(section.id, question.id, answer)
                }
                Concept.DATE_NONE,
                Concept.DATE_NONE_CONTINUE,
                Concept.DATE_NONE_TAKE,
                Concept.DATE -> DateQuestion(
                    question = question,
                    enabled = enabled,
                    onAnswerCheckedChange = { answer ->
                        viewModel.saveQuestionAnswer(section.id, question.id, answer)
                    }
                )
                Concept.TEXTAREA -> TextAreaQuestion(question, enabled) { answer ->
                    viewModel.saveQuestionAnswer(section.id, question.id, answer)
                }
                Concept.CHECKBOX -> Unit
                Concept.INFO -> InfoQuestion(question)
                else -> Unit
            }
        }

        // 약물 추가
        if (section.id == 3) {
            if (section.questionList.isNotEmpty() && section.questionList[0].selectedAnswer.seq == 1) {
                drugList.forEach { drug ->
                    DrugQuestion(
                        drug = drug,
                        enabled = enabled,
                        onDrugChange = { questionId, answer ->
                            viewModel.onDrugChange(drug.id, questionId, answer)
                        },
                        onDrugRemove = {
                            viewModel.onDrugRemove(drug)
                        }
                    )
                }

                if (enabled) {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 8.dp),
                    ) {
                        Button(
                            enabled = enabled,
                            onClick = viewModel::onAddDrugClick
                        ) {
                            Text(text = "약물 추가")
                        }
                    }
                }
            }
        }

        val isLastPage = viewModel.isLastPage(section.id)

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(8.dp),
            onClick = onNextButtonClick,
            enabled = !isLastPage || enabled,
        ) {
            if (isLastPage) {
                Text(text = "저장")
            } else {
                Text(text = "다음")
            }
        }
    }
}

@Composable
fun SurveyInfo(viewModel: ScreeningViewModel) {
    val name by viewModel.name.observeAsState("")
    val birth by viewModel.birth.observeAsState("")
    val gender by viewModel.gender.observeAsState(1)

    val enabled by viewModel.enabledUpdate.observeAsState(true)

    val nameQuestion = Question(groupId = 0,
        id = 0,
        seq = 0,
        content = "실명을 입력해주세요.",
        screeningQuestionId = 0,
        answerItemGroupId = 0,
        answerConcept = Concept.TEXTAREA,
        answerItemList = listOf(),
        selectedAnswer = AnswerItem.text(name),
        surveySectionId = 0)

    val birthQuestion = Question(groupId = 0,
        id = 0,
        seq = 0,
        content = "생년월일을 입력해주세요.",
        screeningQuestionId = 0,
        answerItemGroupId = 0,
        answerConcept = Concept.DATE,
        answerItemList = listOf(),
        selectedAnswer = AnswerItem.text(birth),
        surveySectionId = 0
    )

    val genderQuestion = Question(groupId = 0,
        id = 0,
        seq = 0,
        content = "성별을 입력해주세요.",
        screeningQuestionId = 0,
        answerItemGroupId = 0,
        answerConcept = Concept.RADIO,
        answerItemList = listOf(
            AnswerItem(groupId = 2, id = 14, seq = 1, text = "남자"),
            AnswerItem(groupId = 2, id = 15, seq = 2, text = "여자"),
        ),
        selectedAnswer = if (gender == 1) {
            AnswerItem(groupId = 2, id = 14, seq = 1, text = "남자")
        } else {
            AnswerItem(groupId = 2, id = 15, seq = 2, text = "여자")
        },
        surveySectionId = 0
    )

    Column {
        TextAreaQuestion(
            question = nameQuestion,
            enabled = enabled,
            onAnswerChange = {
                viewModel.changeName(it.text)
            }
        )
        DateQuestion(
            question = birthQuestion,
            enabled = enabled,
            onAnswerCheckedChange = {
                viewModel.changeBirth(it.text)
            }
        )
        RadioQuestion(
            question = genderQuestion,
            enabled = enabled,
            onClick = {
                viewModel.changeGender(if (it.seq == 1) 1 else 0)
            }
        )
    }
}

@Preview
@Composable
fun LoadingPreview() {
    MdcTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CircularProgressIndicator(modifier = Modifier.size(56.dp), progress = 0.7f)
        }
    }
}