package com.devmon.crcp.data.network.response


import com.google.gson.annotations.SerializedName

data class BaseResponse(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("output")
    val output: String?,
    @SerializedName("rows")
    val rows: Int?
)