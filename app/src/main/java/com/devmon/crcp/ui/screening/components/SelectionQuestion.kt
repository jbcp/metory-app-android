package com.devmon.crcp.ui.screening.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.devmon.crcp.R
import com.devmon.crcp.domain.model.AnswerItem
import com.devmon.crcp.domain.model.Concept
import com.devmon.crcp.domain.model.Question
import com.google.android.material.composethemeadapter.MdcTheme

@Composable
fun SelectionQuestion(
    question: Question,
    enabled: Boolean = true,
    onClick: (answerItem: AnswerItem) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = question.content,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.color_black),
        )

        Spacer(modifier = Modifier.height(12.dp))

        ConstraintLayout(
            constraints(question.answerItemList.size),
            modifier = Modifier.fillMaxWidth()
        ) {
            question.answerItemList.forEachIndexed { i, it ->
                val splitTextList = splitAnswerItemText(it.text)

                RadioButton(
                    selected = question.selectedAnswer == it,
                    enabled = enabled,
                    onClick = {
                        onClick(it)
                    },
                    modifier = Modifier.layoutId("radio$i")
                )

                if (i != question.answerItemList.size - 1) {
                    DashDivider(
                        color = lineColor(enabled),
                        modifier = Modifier.layoutId("divider$i"),
                        dashWidth = 4.dp,
                        dashGap = 2.dp,
                    )
                }

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.layoutId("text$i")
                ) {

                    Spacer(Modifier.size(4.dp))

                    splitTextList.forEach { text ->
                        Text(
                            text = text,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                }

                Box(modifier = Modifier.layoutId("box$i").clickable(enabled) { onClick(it) }) {}
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Divider(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        )
    }
}

private fun lineColor(enabled: Boolean): Color {
    return if (enabled) {
        "#6f6f6f".color
    } else {
        "#a5a5a5".color
    }
}

val String.color
    get() = Color(android.graphics.Color.parseColor(this))

private fun constraints(itemCount: Int): ConstraintSet {
    return ConstraintSet {
        val boxRefs = (0 until itemCount).map { createRefFor("box$it") }
        val textRefs = (0 until itemCount).map { createRefFor("text$it") }
        val radioRefs = (0 until itemCount).map { createRefFor("radio$it") }

        repeat(itemCount) {
            val box = boxRefs[it]
            val radio = radioRefs[it]
            val text = textRefs[it]

            constrain(box) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                height = Dimension.fillToConstraints
                width = Dimension.fillToConstraints
            }
            constrain(radio) {
                top.linkTo(parent.top)
                start.linkTo(box.start)
                end.linkTo(box.end)
                bottom.linkTo(text.top, 4.dp)
            }
            constrain(text) {
                start.linkTo(box.start)
                end.linkTo(box.end)
                bottom.linkTo(parent.bottom)
            }

            if (it != itemCount - 1) {
                val divider = createRefFor("divider$it")
                constrain(divider) {
                    top.linkTo(radio.top)
                    bottom.linkTo(radio.bottom)
                    start.linkTo(radio.end, 2.dp)
                    end.linkTo(radioRefs[it + 1].start, 2.dp)
                    width = Dimension.fillToConstraints
                }
            }
        }

        createHorizontalChain(*boxRefs.toTypedArray())
    }
}

fun splitAnswerItemText(text: String): List<String> {
    val splitTextList = mutableListOf<String>()
    splitTextList.add(text.substringBefore("("))
    val afterText = text.substringAfter("(", "")
    if (afterText.isNotEmpty()) {
        splitTextList.add("($afterText")
    } else {
        splitTextList.add("")
    }
    return splitTextList
}

@Preview(showBackground = true)
@Composable
fun SelectionQuestionPreview() {
    val question = Question(groupId = 0,
        id = 0,
        seq = 0,
        content = "권태감/피로감",
        screeningQuestionId = 0,
        answerItemGroupId = 0,
        answerConcept = Concept.SELECTION,
        answerItemList = listOf(
            AnswerItem(groupId = 0, id = 0, seq = 0, text = "없음"),
            AnswerItem(groupId = 0, id = 0, seq = 0, text = "조금(mild)"),
            AnswerItem(groupId = 0, id = 0, seq = 0, text = "보통(moderate)"),
            AnswerItem(groupId = 0, id = 0, seq = 0, text = "많이(severe)"),
        ),
        selectedAnswer = AnswerItem(groupId = 0, id = 0, seq = 0, text = "조금(mild)"),
        surveySectionId = 0
    )

    MdcTheme {
        SelectionQuestion(question, true) {}
    }
}

