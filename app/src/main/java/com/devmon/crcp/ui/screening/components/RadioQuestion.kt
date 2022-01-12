package com.devmon.crcp.ui.screening.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devmon.crcp.R
import com.devmon.crcp.domain.model.AnswerItem
import com.devmon.crcp.domain.model.Concept
import com.devmon.crcp.domain.model.Question
import com.google.android.material.composethemeadapter.MdcTheme

@Composable
fun RadioQuestion(
    question: Question,
    enabled: Boolean = true,
    onClick: (answerItem: AnswerItem) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .padding(bottom = 8.dp),
        ) {
            Text(
                text = question.content,
                color = colorResource(id = R.color.color_screening_label),
                modifier = Modifier.weight(4f)
            )

            question.answerItemList.forEach {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxHeight()
                        .clickable(
                            enabled = enabled,
                            onClick = {
                                onClick(it)
                            }
                        ),
                ) {
                    RadioButton(
                        selected = question.selectedAnswer == it,
                        enabled = enabled,
                        onClick = {
                            onClick(it)
                        }
                    )
                    Text(
                        text = it.text,
                        color = colorResource(id = R.color.color_screening_label),
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
            }
        }

        Divider(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RadioQuestionPreview() {
    val question = Question(groupId = 0,
        id = 0,
        seq = 0,
        content = "성별을 입력하세요성별을 입력하세요성별을 입력하세요성별을 입력하세요성별을 입력하세요성별을 입력하세요",
        screeningQuestionId = 0,
        answerItemGroupId = 0,
        answerConcept = Concept.RADIO,
        answerItemList = listOf(
            AnswerItem(groupId = 0, id = 0, seq = 0, text = "예"),
            AnswerItem(groupId = 0, id = 0, seq = 0, text = "아니오"),
        ),
        selectedAnswer = AnswerItem(groupId = 0, id = 0, seq = 0, text = "예"),
        surveySectionId = 0
    )

    MdcTheme {
        RadioQuestion(question, true) {}
    }
}