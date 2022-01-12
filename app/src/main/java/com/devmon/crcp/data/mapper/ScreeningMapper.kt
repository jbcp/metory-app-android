package com.devmon.crcp.data.mapper

import com.devmon.crcp.data.network.response.ScreeningResponse
import com.devmon.crcp.domain.model.Screening
import com.devmon.crcp.domain.model.AnswerItem
import com.devmon.crcp.domain.model.Concept
import com.devmon.crcp.domain.model.Question
import com.devmon.crcp.domain.model.SurveySection

fun ScreeningResponse.toDomain(): Screening {

    val devmonQuestionIdList = (devmonQuestionList ?: emptyList())
        .map { it.screeningQuestionId ?: -1 }
        .filter { it != -1 }

    val answerItemList = (ref?.answerItem ?: emptyList())
        .map {
            AnswerItem(
                groupId = it.answerItemGroupId ?: -1,
                id = it.answerItemId ?: -1,
                seq = it.answerItemSeq ?: -1,
                text = it.answerItemText ?: "",
            )
        }
        .toMutableList()

    answerItemList.addAll(
        listOf(
            AnswerItem(groupId = 4, id = 9, seq = 1, text = "해당 없음"),
            AnswerItem(groupId = 5, id = 10, seq = 1, text = "복용 중"),
            AnswerItem(groupId = 6, id = 11, seq = 1, text = "해당 없음"),
            AnswerItem(groupId = 6, id = 12, seq = 2, text = "지속 중"),
        )
    )

    val subQuestionList = (ref?.screeningSubQuestion ?: emptyList())
        .map {
            val concept = Concept.of(it.answerTypeConceptId)
            Question(
                groupId = it.questionGroupId ?: -1,
                id = it.subQuestionSeq ?: -1,
                seq = it.subQuestionSeq ?: -1,
                content = it.subQuestionContent ?: "",
                screeningQuestionId = it.subQuestionSeq ?: -1,
                answerItemGroupId = it.answerItemGroupId ?: -1,
                answerConcept = concept,
                answerItemList = filterAnswerItemListByConcept(0, answerItemList, concept),
                surveySectionId = 0,
            )
        }

    val sectionList = (screeningSet?.screeningQuestion ?: emptyList())
        .asSequence()
        .filter { it.surveySectionId != null }
        .filter { devmonQuestionIdList.contains(it.screeningQuestionId) }
        .groupBy { it.surveySectionId }
        .filter { it.value.isNotEmpty() }
        .map { (sectionId, questionList) ->
            val question = questionList[0]
            SurveySection(
                id = sectionId ?: -1,
                seq = question.sectionSeq ?: -1,
                name = question.surveySectionName ?: "",
                info = "",
                questionList = questionList.map {
                    val concept = Concept.of(it.answerTypeConceptId)
                    Question(
                        groupId = it.questionGroupId ?: 0,
                        id = it.questionId ?: -1,
                        seq = it.questionSeq ?: -1,
                        content = it.questionContent ?: "",
                        screeningQuestionId = it.screeningQuestionId ?: -1,
                        answerItemGroupId = it.answerItemGroupId ?: -1,
                        answerConcept = concept,
                        answerItemList = filterAnswerItemListByConcept(it.answerItemGroupId ?: 0, answerItemList, concept),
                        surveySectionId = it.surveySectionId ?: -1,
                    )
                }.sortedBy { it.seq },
            )
        }
        .sortedBy { it.seq }

    return Screening(
        sectionList,
        subQuestionList,
        answerItemList,
    )
}

fun filterAnswerItemListByConcept(
    answerItemGroupId: Int,
    allAnswerItemList: List<AnswerItem>,
    concept: Concept,
): List<AnswerItem> {
    return when (concept) {
        Concept.NONE -> emptyList()
        Concept.RADIO -> allAnswerItemList.filter { it.groupId == answerItemGroupId }
        Concept.TEXT -> emptyList()
        Concept.SELECTION -> emptyList()
        Concept.DATE -> emptyList()
        Concept.TEXTAREA -> emptyList()
        Concept.CHECKBOX -> allAnswerItemList.filter { it.groupId == answerItemGroupId }
        Concept.INFO -> emptyList()
        Concept.DATE_NONE -> allAnswerItemList.filter { it.groupId == 4 }
        Concept.DATE_NONE_TAKE -> allAnswerItemList.filter { it.groupId == 5 }
        Concept.DATE_NONE_CONTINUE -> allAnswerItemList.filter { it.groupId == 6 }
        Concept.DAILY_DRUG -> emptyList()
    }
}
