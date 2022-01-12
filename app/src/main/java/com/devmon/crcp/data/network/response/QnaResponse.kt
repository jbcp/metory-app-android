package com.devmon.crcp.data.network.response


import com.google.gson.annotations.SerializedName

data class QnaResponse(
        @SerializedName("SAID")
        val said: Int?, // 25
        @SerializedName("INVID")
        val invid: Any?, // null
        @SerializedName("QNADTC")
        val qnadtc: String?, // 2021-05-09 15:35:36
        @SerializedName("QNACONTENT")
        val qnacontent: String?, // test
        // subj_flag 1: 자원자, 2: 연구자
        @SerializedName("SUBJ_FLAG")
        val subjflag: Int?, // 1
)