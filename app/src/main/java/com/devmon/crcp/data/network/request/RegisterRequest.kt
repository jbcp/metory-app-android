package com.devmon.crcp.data.network.request


import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("birth")
    val birth: String = "",
    @SerializedName("email")
    val email: String = "",
    @SerializedName("hp")
    val hp: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("pwd")
    val pwd: String = "",
    @SerializedName("sex")
    val sex: Int = 1,
)