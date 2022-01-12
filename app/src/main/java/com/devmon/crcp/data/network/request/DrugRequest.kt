package com.devmon.crcp.data.network.request

import com.google.gson.annotations.SerializedName

data class DrugRequest(
    @SerializedName("applid")
    val applid: String,
    @SerializedName("datetime")
    val datetime: String,
    @SerializedName("response_answer")
    val responseAnswer: List<ResponseAnswer>,
    @SerializedName("response_sub_answer")
    val responseSubAnswer: List<ResponseSubAnswer>,
    @SerializedName("said")
    val said: String,
    @SerializedName("screening_id")
    val screeningId: String
) {
    data class ResponseAnswer(
        @SerializedName("answer")
        val answer: String,
        @SerializedName("screening_question_id")
        val screeningQuestionId: String
    )

    data class ResponseSubAnswer(
        @SerializedName("answer")
        val answer: String,
        @SerializedName("question_group_id")
        val questionGroupId: Int,
        @SerializedName("sub_question_id")
        val subQuestionId: Int
    )
}