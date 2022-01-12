package com.devmon.crcp.ui.screening.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmon.crcp.R
import com.devmon.crcp.domain.model.AnswerItem
import com.devmon.crcp.domain.model.Concept
import com.devmon.crcp.domain.model.Question
import com.devmon.crcp.ui.screening.showDatePicker
import com.google.android.material.composethemeadapter.MdcTheme
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.launch

@Composable
fun DateQuestion(
    question: Question,
    enabled: Boolean = true,
    onAnswerCheckedChange: (answerItem: AnswerItem) -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val isDateMode = question.selectedAnswer.id == 0
    val date = if (isDateMode) {
        question.selectedAnswer.text
    } else {
        ""
    }

    val showDatePicker = {
        coroutineScope.launch {
            val resultDate = suspendCoroutine<String> { cont ->
                showDatePicker(
                    context,
                    date,
                    onDateSelect = {
                        cont.resume(it)
                    },
                    onCancel = {
                        cont.resume("")
                    }
                )
            }

            if (resultDate.isNotEmpty()) {
                onAnswerCheckedChange(AnswerItem.text(resultDate))
            }
        }
        Unit
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = question.content)

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp, bottom = 8.dp)
                    .clickable(
                        enabled = isDateMode && enabled,
                        onClick = showDatePicker
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_date_range_24),
                    contentDescription = "date",
                    modifier = Modifier.padding(end = 8.dp)
                )

                if (date.isEmpty() && isDateMode) {
                    Text(text = "여기를 눌러주세요")
                } else {
                    Text(text = date)
                }
            }

            question.answerItemList.forEach {
                val onCheckedChange: (Boolean) -> Unit = { checked ->
                    if (checked) {
                        onAnswerCheckedChange(it)
                    } else {
                        val answerItem = AnswerItem.NONE
                        onAnswerCheckedChange(answerItem)
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable(enabled = enabled) {
                            onCheckedChange(question.selectedAnswer != it)
                        }
                ) {
                    Checkbox(
                        checked = question.selectedAnswer == it,
                        enabled = enabled,
                        onCheckedChange = onCheckedChange
                    )
                    Text(
                        text = it.text,
                        fontSize = 11.sp,
                        color = colorResource(id = R.color.color_screening_label),
                        modifier = Modifier.padding(start = 4.dp),
                    )
                }
            }
        }

        Divider(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        )
    }
}

@Preview
@Composable
fun DateQuestionPreview() {
    val question = Question(groupId = 0,
        id = 0,
        seq = 0,
        content = "COVID-19 증상 시작일을 기입하시오.",
        screeningQuestionId = 0,
        answerItemGroupId = 0,
        answerConcept = Concept.DATE,
        answerItemList = listOf(
            AnswerItem(groupId = 5, id = 9, seq = 1, text = "해당 없음"),
            AnswerItem(groupId = 5, id = 10, seq = 2, text = "지속 중"),
        ),
        surveySectionId = 0
    )
    MdcTheme {
        DateQuestion(question, true) {}
    }
}