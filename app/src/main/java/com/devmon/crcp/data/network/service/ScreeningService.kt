package com.devmon.crcp.data.network.service

import com.devmon.crcp.data.network.request.ScreeningRequest
import com.devmon.crcp.data.network.response.CrcpResponse
import com.devmon.crcp.data.network.response.ScreeningResponse
import com.devmon.crcp.data.network.response.SurveyLoadResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ScreeningService {

    @FormUrlEncoded
    @POST("/screening")
    suspend fun screening(
        @Field("sid") sid: Int,
    ): ScreeningResponse

    @POST("/screening/save")
    suspend fun saveSurvey(
        @Body screening: ScreeningRequest,
    ): CrcpResponse<String>

    @FormUrlEncoded
    @POST("/screening/load")
    suspend fun loadSurvey(
        @Field("said") said: Int,
    ): CrcpResponse<SurveyLoadResponse>
}