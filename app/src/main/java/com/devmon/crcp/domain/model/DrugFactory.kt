package com.devmon.crcp.domain.model

class DrugFactory {
    private val questionList = mutableListOf<Question>()

    fun replaceAll(questions: List<Question>) {
        questionList.clear()
        questionList.addAll(questions)
    }

    fun createDrug(drugId: Int): Drug {
        return Drug(
            id = drugId,
            questionList = questionList.map { it.copy() },
        )
    }
}