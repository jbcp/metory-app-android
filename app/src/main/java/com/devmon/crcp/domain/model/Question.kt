package com.devmon.crcp.domain.model

import com.google.gson.Gson

data class Question(
    /**
     * 질문지와 연결 ID
     */
    val groupId: Int,
    val id: Int,
    val seq: Int,
    /**
     * 질문 내용
     */
    val content: String,
    /**
     * 자가진단 질문 id (DB에서 사용되는 값)
     */
    val screeningQuestionId: Int,
    /**
     * 질문유형그룹 ID (그룹으로 질문 갯수를 알수 있음)
     */
    val answerItemGroupId: Int,
    /**
     * 응답유형 concept_id
     */
    val answerConcept: Concept,
    val answerItemList: List<AnswerItem>,
    val selectedAnswer: AnswerItem = AnswerItem.NONE,
    /**
     * 질문 그룹 Id
     */
    val surveySectionId: Int,
    val required: Boolean = true,
)

data class SurveySection(
    val id: Int,
    val seq: Int,
    val name: String,
    val info: String,
    val questionList: List<Question>,
)

data class Drug(
    val id: Int,
    val questionList: List<Question>,
)

enum class Concept(val id: Int) {
    NONE(0),
    RADIO(1001),
    TEXT(1002),
    SELECTION(1003),
    DATE(1004),
    TEXTAREA(1005),
    CHECKBOX(1006),
    INFO(1007),
    DATE_NONE(1008),
    DATE_NONE_TAKE(1009),
    DATE_NONE_CONTINUE(1010),
    DAILY_DRUG(1011),
    ;

    companion object {
        fun of(id: Int?): Concept {
            if (id == null) {
                return NONE
            }

            for (concept in values()) {
                if (concept.id == id) {
                    return concept
                }
            }

            return NONE
        }
    }
}

data class DailyDrugAnswer(
    val dayCount: Int,
    val drugCapacity: String,
    val drugType: String,
)

fun DailyDrugAnswer.toText(): String {
    return Gson().toJson(this)
}

fun String.toDailyDrugAnswer(): DailyDrugAnswer {
    return try {
        Gson().fromJson(this, DailyDrugAnswer::class.java)
    } catch (e: Exception) {
        DailyDrugAnswer(
            dayCount = 1,
            drugCapacity = "",
            drugType = "알",
        )
    }
}

data class AnswerItem(
    val groupId: Int,
    val id: Int,
    val seq: Int,
    val text: String,
) {
    companion object {
        val NONE = AnswerItem(groupId = 0, id = 0, seq = 0, text = "")

        fun text(text: String) = AnswerItem(groupId = 0, id = 0, seq = 0, text = text)
    }
}