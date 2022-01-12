package com.devmon.crcp.domain.repository

import com.devmon.crcp.data.network.request.ChangePasswordRequest
import com.devmon.crcp.data.network.request.LoginRequest
import com.devmon.crcp.data.network.request.RegisterRequest
import com.devmon.crcp.data.network.request.UserJoinRequest
import com.devmon.crcp.data.network.response.ApiInfoResponse
import com.devmon.crcp.data.network.response.BaseResponse
import com.devmon.crcp.data.network.response.CrcpResponse
import com.devmon.crcp.data.network.response.UserResponse
import com.devmon.crcp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface MemberRepository {

    fun requestRegister(userInfo: RegisterRequest): Flow<CrcpResponse<String>>

    fun userJoin(userJoinRequest: UserJoinRequest): Flow<CrcpResponse<String>>

    fun requestLogin(loginRequest: LoginRequest): Flow<CrcpResponse<UserResponse>>

    fun setAutoLogin(isAutoLogin: Boolean)

    fun getAutoLogin(): Boolean

    fun setUserInfo(userResponse: UserResponse?)

    fun changePassword(changePasswordRequest: ChangePasswordRequest): Flow<BaseResponse>

    suspend fun getUserInfo(): UserResponse?

    suspend fun detail(applid: Int): User

    suspend fun getRegisterTerms(): Flow<CrcpResponse<String>>

    suspend fun updateUserInfo(
        applid: String,
        applpwd: String,
        applsex: Int,
        applphonenum: String,
        applbrthdtc: String,
    ): Flow<CrcpResponse<String>>

    suspend fun getPassword(): String
    suspend fun setPassword(password: String)

    suspend fun sendPushToken(
        email: String,
        token: String,
    ): Flow<CrcpResponse<String>>

    suspend fun getApiInfo(): Flow<CrcpResponse<ApiInfoResponse>>

    fun saveSiteIp(ip: String)

    fun getSiteIp(): String

    fun saveSubjectIp(ip: String)

    fun getSubjectIp(): String
}