package com.devmon.crcp.domain.repository

import com.devmon.crcp.data.network.request.ScreeningRequest
import com.devmon.crcp.data.network.response.CrcpResponse
import com.devmon.crcp.data.network.response.ScreeningResponse
import com.devmon.crcp.data.network.response.SurveyLoadResponse
import kotlinx.coroutines.flow.Flow

interface ScreeningRepository {

    fun load(said: Int): Flow<CrcpResponse<SurveyLoadResponse>>

    fun save(screening: ScreeningRequest): Flow<CrcpResponse<String>>

    fun getScreening(sid: Int): Flow<CrcpResponse<ScreeningResponse>>
}