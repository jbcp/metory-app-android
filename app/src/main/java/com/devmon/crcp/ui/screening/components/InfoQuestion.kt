package com.devmon.crcp.ui.screening.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmon.crcp.domain.model.Concept
import com.devmon.crcp.domain.model.Question
import com.google.android.material.composethemeadapter.MdcTheme

@Composable
fun InfoQuestion(question: Question) {
    Text(
        text = question.content,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    )
}

@Preview
@Composable
fun InfoQuestionPreview() {
    val question = Question(
        groupId = 0,
        id = 0,
        seq = 0,
        content = "다음 증상 중 해당하는 것의 정도를 선택하시오.",
        screeningQuestionId = 0,
        answerItemGroupId = 0,
        answerConcept = Concept.INFO,
        answerItemList = listOf(),
        surveySectionId = 0,
    )

    MdcTheme {
        InfoQuestion(question)
    }
}