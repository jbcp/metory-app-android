package com.devmon.crcp.data.network.request


import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("newpwd")
    val newpwd: String,
    @SerializedName("newpwd2")
    val newpwd2: String,
    @SerializedName("pwd")
    val pwd: String
)