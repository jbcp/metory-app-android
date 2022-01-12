package com.devmon.crcp.data.network.response


import com.google.gson.annotations.SerializedName

data class DefaultResponse<T>(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("output")
    val output: T?,
    @SerializedName("rows")
    val rows: Int?
) {
    fun isNotEmpty() = (rows ?: 0) > 0
}