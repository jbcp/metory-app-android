package com.devmon.crcp.data.network.response

import com.devmon.crcp.ui.model.Recruiting
import com.google.gson.annotations.SerializedName

data class RecruitingResponse(
    @SerializedName("SID")
    val sid: Int?,
    @SerializedName("TITLE")
    val title: String?,
    @SerializedName("SAPPL")
    val sappl: String?,
    @SerializedName("SSEX")
    val ssex: Int?,
    @SerializedName("SNUM")
    val snum: Int?,
    @SerializedName("STARGET")
    val starget: String?,
    @SerializedName("SDATE")
    val sdate: String?,
    @SerializedName("EMERGENCY")
    val emergency: Int?,
    @SerializedName("SITENAME")
    val sitename: String?,
    @SerializedName("SFILE")
    val sfile: String?,
    @SerializedName("SACTIVE")
    val sactive: Int?,
) {
    fun toUi(siteIp: String) = Recruiting(
        sid = sid,
        title = title,
        sappl = sappl,
        ssex = ssex,
        imageUrl = "$siteIp$sfile",
        snum = snum ?: 0,
        starget = starget ?: "",
        sdate = sdate,
        emergency = emergency,
        sitename = sitename,
        sactive = sactive == 1,
    )
}