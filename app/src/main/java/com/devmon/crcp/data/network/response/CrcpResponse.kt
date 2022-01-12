package com.devmon.crcp.data.network.response

import com.google.gson.annotations.SerializedName

data class CrcpResponse<T>(
    @SerializedName("code")
    val code: String?,
    @SerializedName("rows")
    val rows: Int?,
    @SerializedName("data")
    val data: T?,
    @SerializedName("error")
    val error: String?
) {
    fun isError(): Boolean {
        return error != null
    }

    fun isSuccessful(): Boolean {
        return data != null && rows != 0
    }

    companion object {
        fun error(error: String? = null) = CrcpResponse(null, null, null, error)
    }
}