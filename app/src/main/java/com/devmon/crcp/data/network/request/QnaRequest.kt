package com.devmon.crcp.data.network.request

import com.google.gson.annotations.SerializedName

data class QnaRequest(
    @SerializedName("applid")
    val applid: Int,
    @SerializedName("said")
    val said: Int,
    @SerializedName("qnacontent")
    val qnaContent: String,
)