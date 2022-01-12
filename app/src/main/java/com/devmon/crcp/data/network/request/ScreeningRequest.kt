package com.devmon.crcp.data.network.request

import com.google.gson.annotations.SerializedName

data class ScreeningRequest(
    @SerializedName("said")
    val said: String,
    @SerializedName("applid")
    val applid: String,
    @SerializedName("screening_id")
    val screeningId: String,
    @SerializedName("datetime")
    val datetime: String,
    @SerializedName("response_answer")
    val list: List<AnswerRequest>,
    @SerializedName("response_sub_answer")
    val subList: List<DrugRequest.ResponseSubAnswer>,
)

data class AnswerRequest(
    @SerializedName("screening_question_id")
    val id: Int,
    @SerializedName("answer")
    val body: String,
)