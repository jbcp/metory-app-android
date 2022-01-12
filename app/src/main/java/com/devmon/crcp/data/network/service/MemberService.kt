package com.devmon.crcp.data.network.service

import com.devmon.crcp.data.network.request.ChangePasswordRequest
import com.devmon.crcp.data.network.request.LoginRequest
import com.devmon.crcp.data.network.request.RegisterRequest
import com.devmon.crcp.data.network.request.UserJoinRequest
import com.devmon.crcp.data.network.response.ApiInfoResponse
import com.devmon.crcp.data.network.response.BaseResponse
import com.devmon.crcp.data.network.response.CrcpResponse
import com.devmon.crcp.data.network.response.UserResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface MemberService {

    @POST("/user")
    suspend fun requestRegister(@Body userInfo: RegisterRequest): CrcpResponse<String>

    @POST("/user/join")
    suspend fun userJoin(@Body userJoinRequest: UserJoinRequest): CrcpResponse<String>

    @POST("/user/login")
    suspend fun requestLogin(@Body loginRequest: LoginRequest): CrcpResponse<UserResponse>

    @FormUrlEncoded
    @POST("user/appl-detail")
    suspend fun detail(
        @Field("applid") applid: Int,
    ): CrcpResponse<List<UserResponse>>

    @GET("/info/signup-privacy")
    suspend fun getRegisterTerms(): CrcpResponse<String>

    @POST("/user/change-pwd")
    suspend fun changePassword(@Body changePasswordRequest: ChangePasswordRequest): BaseResponse

    @FormUrlEncoded
    @POST("/user/appl-modify")
    suspend fun updateUserInfo(
        @Field("applid") applid: String,
        @Field("applpwd") applpwd: String,
        @Field("applsex") applsex: Int,
        @Field("applphonenum") applphonenum: String,
        @Field("applbrthdtc") applbrthdtc: String,
    ): CrcpResponse<String>

    @POST("/info/ip")
    suspend fun getApiInfo(): CrcpResponse<ApiInfoResponse>
}