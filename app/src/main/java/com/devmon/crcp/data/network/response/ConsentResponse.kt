package com.devmon.crcp.data.network.response

import com.google.gson.annotations.SerializedName

data class ConsentResponse(
    @SerializedName("CONSENTID")
    val consentid: Int, // 7
    @SerializedName("SID")
    val sid: Int?, // 3
    @SerializedName("SITEID")
    val siteid: Int?, // 1
    @SerializedName("INVID")
    val invid: Int?, // 1
    @SerializedName("CSGRPID")
    val csgrpid: Int?, // 5
    @SerializedName("CVERSION")
    val cversion: String?, // v1.0
    @SerializedName("CFILE")
    val cfile: String?, // /upload/1/c_1611542883740.pdf
    @SerializedName("CFILENAME")
    val cfilename: String?, // KINECT_01_별첨2_시험대상자 동의서 및 설명서_v1.0_20170908.pdf
    @SerializedName("ISPUBLISH")
    val ispublish: Int?, // 1
    @SerializedName("CPUBLISHDTC")
    val cpublishdtc: String?, // 2021-01-25 11:48:09
    @SerializedName("CONTACT_COPTION")
    val contactcoption: Int?, // 1
    @SerializedName("INV_SIGN_COPTION")
    val invsigncoption: Int?, // 1
    @SerializedName("RECENT_PUBLISHED")
    val recentpublished: Int?, // 0
    @SerializedName("CFILE_HASH")
    val cfilehash: String? // 2506c4326b4e20a4d1ac9c0b5033d5024a35a57cf6fccbb5bec5efd605f17dd7
)