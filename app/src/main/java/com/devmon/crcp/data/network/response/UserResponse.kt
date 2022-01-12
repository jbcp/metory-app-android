package com.devmon.crcp.data.network.response


import com.devmon.crcp.domain.model.User
import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("APPLID")
    val applid: Int?,
    @SerializedName("APPLNAME")
    val applname: String?,
    @SerializedName("APPLMAIL")
    val applmail: String?,
    @SerializedName("APPLPHONENUM")
    val applphonenum: String?,
    @SerializedName("APPLBRTHDTC")
    val applbrthdtc: String?,
    @SerializedName("APPLSEX")
    val applsex: Int?,
    @SerializedName("QRCODE")
    val qrcode: String?
)

fun UserResponse.toUi() = User(
    applid = applid ?: 0,
    applname = applname ?: "",
    applmail = applmail ?: "",
    applphonenum = applphonenum ?: "",
    applbrthdtc = applbrthdtc ?: "",
    applsex = applsex ?: 1,
    qrcode = qrcode ?: ""
)

fun User.toResponse() = UserResponse(
    applid = applid,
    applname = applname,
    applmail = applmail,
    applphonenum = applphonenum,
    applbrthdtc = applbrthdtc,
    applsex = applsex,
    qrcode = qrcode
)