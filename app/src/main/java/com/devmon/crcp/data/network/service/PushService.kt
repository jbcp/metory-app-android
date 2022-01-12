package com.devmon.crcp.data.network.service

import com.devmon.crcp.data.network.response.CrcpResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PushService {

    @FormUrlEncoded
    @POST("push/saveToken")
    suspend fun updatePushToken(
        @Field("email") email: String,
        @Field("token") token: String,
        // 1 로 보내면 푸시 알림이 옴.
        @Field("chk") chk: Int,
    ): CrcpResponse<String>
}