package com.devmon.crcp.data.datasource

import com.devmon.crcp.data.network.response.UserResponse

interface MemberDataSource {
    fun setAutoLogin(isAutoLogin: Boolean)
    fun getAutoLogin(): Boolean
    fun setUserInfo(userResponse: UserResponse?)
    fun getUserInfo(): UserResponse?
    fun getPassword(): String
    fun setPassword(password: String)
    fun saveSiteIp(ip: String)
    fun getSiteIp(): String
    fun saveSubjectIp(ip: String)
    fun getSubjectIp(): String
}
