package com.devmon.crcp.domain.model

data class Screening(
    val sectionList: List<SurveySection>,
    val subQuestionList: List<Question>,
    val answerItemList: List<AnswerItem>,
)