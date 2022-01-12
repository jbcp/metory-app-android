package com.devmon.crcp.domain.model

data class User(
    val applid: Int,
    val applname: String,
    val applmail: String,
    val applphonenum: String,
    val applbrthdtc: String,
    val applsex: Int,
    val qrcode: String
) {
    companion object {
        val EMPTY = User(
            applid = 0,
            applname = "",
            applmail = "",
            applphonenum = "",
            applbrthdtc = "",
            applsex = 0,
            qrcode = ""
        )
    }
}