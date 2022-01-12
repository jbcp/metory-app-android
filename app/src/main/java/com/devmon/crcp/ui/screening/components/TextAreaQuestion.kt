package com.devmon.crcp.ui.screening.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devmon.crcp.domain.model.AnswerItem
import com.devmon.crcp.domain.model.Concept
import com.devmon.crcp.domain.model.Question
import com.google.android.material.composethemeadapter.MdcTheme

@Composable
fun TextAreaQuestion(question: Question, enabled: Boolean = true, onAnswerChange: (answer: AnswerItem) -> Unit) {
    OutlinedTextField(
        value = question.selectedAnswer.text,
        enabled = enabled,
        onValueChange = { value ->
            val answer = AnswerItem(groupId = 0, id = 0, seq = 0, text = value)
            onAnswerChange(answer)
        },
        maxLines = 1,
        label = { Text(question.content) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    )
}

@Preview
@Composable
fun TextAreaQuestionPreview() {
    val question = Question(
        groupId = 0,
        id = 0,
        seq = 0,
        content = "기타병력(직접 입력하세요)",
        screeningQuestionId = 0,
        answerItemGroupId = 0,
        answerConcept = Concept.TEXTAREA,
        answerItemList = listOf(),
        surveySectionId = 0,
    )

    MdcTheme {
        TextAreaQuestion(question) {}
    }
}