package com.devmon.crcp.data.network.response


import com.google.gson.annotations.SerializedName

data class ApiInfoResponse(
    @SerializedName("site_ip")
    val siteIp: String?, // http://사이트주소
    @SerializedName("subject_ip")
    val subjectIp: String? // http://대상자용사이트주소
)