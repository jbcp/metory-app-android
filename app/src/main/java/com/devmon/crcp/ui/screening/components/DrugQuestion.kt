package com.devmon.crcp.ui.screening.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmon.crcp.R
import com.devmon.crcp.domain.model.AnswerItem
import com.devmon.crcp.domain.model.Concept
import com.devmon.crcp.domain.model.Drug
import com.devmon.crcp.domain.model.Question
import com.devmon.crcp.domain.model.toDailyDrugAnswer
import com.devmon.crcp.domain.model.toText
import com.google.android.material.composethemeadapter.MdcTheme

@Composable
fun DrugQuestion(
    drug: Drug,
    enabled: Boolean = true,
    onDrugChange: (questionId: Int, answerItem: AnswerItem) -> Unit,
    onDrugRemove: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "약물 ${drug.id}",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "지우기",
                fontSize = 12.sp,
                modifier = Modifier.clickable(enabled = enabled, onClick = onDrugRemove)
            )
        }

        drug.questionList.forEach { question ->
            when (question.answerConcept) {
                Concept.RADIO -> Unit
                Concept.SELECTION -> Unit
                Concept.DATE_NONE,
                Concept.DATE_NONE_CONTINUE,
                Concept.DATE_NONE_TAKE,
                Concept.DATE -> DateQuestion(
                    question,
                    enabled,
                    onAnswerCheckedChange = {
                        onDrugChange(question.id, it)
                    }
                )
                Concept.TEXT,
                Concept.TEXTAREA -> TextAreaQuestion(question, enabled) {
                    onDrugChange(question.id, it)
                }
                Concept.CHECKBOX -> Unit
                Concept.INFO -> Unit
                Concept.DAILY_DRUG -> DailyDrugQuestion(question, enabled) {
                    onDrugChange(question.id, AnswerItem.text(it))
                }
                else -> Unit
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DrugQuestionPreview() {
    val drug = Drug(
        id = 1,
        questionList = listOf(
            Question(groupId = 0,
                id = 1,
                seq = 0,
                content = "약물명을 기입하시오.",
                screeningQuestionId = 0,
                answerItemGroupId = 0,
                answerConcept = Concept.TEXTAREA,
                answerItemList = listOf(),
                selectedAnswer = AnswerItem.NONE,
                surveySectionId = 0
            ),
            Question(groupId = 0,
                id = 2,
                seq = 0,
                content = "하루에 투여 받는 양을 기입하시오.",
                screeningQuestionId = 0,
                answerItemGroupId = 0,
                answerConcept = Concept.DAILY_DRUG,
                answerItemList = listOf(),
                selectedAnswer = AnswerItem.text("{dayCount=1, drugCapacity=\"5\", drugType=\"알\"}"),
                surveySectionId = 0
            ),
            Question(groupId = 0,
                id = 3,
                seq = 0,
                content = "약물 투여 시작일을 기입하시오.",
                screeningQuestionId = 0,
                answerItemGroupId = 0,
                answerConcept = Concept.DATE,
                answerItemList = listOf(),
                selectedAnswer = AnswerItem.NONE,
                surveySectionId = 0
            ),
            Question(groupId = 0,
                id = 4,
                seq = 0,
                content = "약물 투여 종료일을 기입하시오.(복용중인 경우 현재 복용 중에 체크)",
                screeningQuestionId = 0,
                answerItemGroupId = 0,
                answerConcept = Concept.DATE,
                answerItemList = listOf(
                    AnswerItem(groupId = 6, id = 11, seq = 1, text = "현재 복용 중")
                ),
                selectedAnswer = AnswerItem.NONE,
                surveySectionId = 0
            ),
        ),
    )

    MdcTheme {
        DrugQuestion(
            drug = drug, onDrugChange = { _, _ -> }, onDrugRemove = {},
        )
    }
}

@Composable
fun DailyDrugQuestion(
    question: Question,
    enabled: Boolean = true,
    onAnswerChange: (dailyDrug: String) -> Unit,
) {
    val modifier = Modifier.padding(4.dp)

    val dailyDrug = question.selectedAnswer.text.toDailyDrugAnswer()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = question.content)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(
                modifier = modifier.padding(start = 20.dp, top = 8.dp, bottom = 8.dp, end = 20.dp),
                text = "1일"
            )

            for (i in 1..4) {

                Text(
                    text = "$i",
                    fontSize = 20.sp,
                    color = selectedColor(dailyDrug.dayCount == i),
                    textAlign = TextAlign.Center,
                    modifier = modifier
                        .weight(1f)
                        .clickable(enabled) {
                            onAnswerChange(dailyDrug
                                .copy(dayCount = i)
                                .toText())
                        },
                )
            }
            Text(modifier = modifier.padding(end = 20.dp), text = "회")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = modifier
                    .weight(1f)
                    .padding(start = 20.dp, top = 8.dp, bottom = 8.dp, end = 20.dp),
                text = "1회"
            )
            TextField(
                modifier = Modifier.width(100.dp),
                value = dailyDrug.drugCapacity,
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    color = colorResource(id = R.color.color_blue_400),
                    textAlign = TextAlign.End,
                ),
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                enabled = enabled,
                onValueChange = {
                    onAnswerChange(dailyDrug.copy(drugCapacity = it.trim()).toText())
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = colorResource(id = R.color.color_grey_200)
                )
            )

            listOf("알", "mg", "ml").forEach {
                Text(
                    text = it,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = selectedColor(dailyDrug.drugType == it),
                    modifier = modifier
                        .padding(start = 10.dp, end = 10.dp)
                        .clickable(enabled) {
                            onAnswerChange(dailyDrug
                                .copy(drugType = it)
                                .toText())
                        },
                )
            }
            Spacer(modifier = Modifier.size(10.dp))
        }
    }
}

@Composable
fun selectedColor(isSelected: Boolean): Color {
    return if (isSelected) {
        colorResource(id = R.color.color_blue_400)
    } else {
        colorResource(id = R.color.color_898989)
    }
}