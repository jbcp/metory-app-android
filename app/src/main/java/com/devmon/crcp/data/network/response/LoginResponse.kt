package com.devmon.crcp.data.network.response


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("output")
    val output: Output,
    @SerializedName("rows")
    val rows: Int
) {
    data class Output(
        @SerializedName("APPLBRTHDTC")
        val aPPLBRTHDTC: String,
        @SerializedName("APPLDATE")
        val aPPLDATE: String,
        @SerializedName("APPLID")
        val aPPLID: Int,
        @SerializedName("APPLMAIL")
        val aPPLMAIL: String,
        @SerializedName("APPLNAME")
        val aPPLNAME: String,
        @SerializedName("APPLPHONENUM")
        val aPPLPHONENUM: String,
        @SerializedName("APPLSEX")
        val aPPLSEX: Int,
        @SerializedName("QRCODE")
        val qRCODE: String
    )
}