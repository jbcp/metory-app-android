package com.devmon.crcp.data.network.response


import com.google.gson.annotations.SerializedName

data class MyStudyResponse(
    @SerializedName("SAID")
    val said: Int?, // 17
    @SerializedName("SID")
    val sid: Int?, // 3
    @SerializedName("APPLNAME")
    val applname: String?, // dy
    @SerializedName("TITLE")
    val title: String?, // COVID19_test1
    @SerializedName("SAPPL")
    val sappl: String?, // 건강한 성인
    @SerializedName("SSEX")
    val ssex: Int?, // 1
    @SerializedName("SITENAME")
    val sitename: String?, // 전북대학교병원
    @SerializedName("SITEID")
    val siteid: Int? // 1
)