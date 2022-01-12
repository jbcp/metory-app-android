package com.devmon.crcp.ui.screening

import com.devmon.crcp.data.network.request.ScreeningRequest
import com.devmon.crcp.data.network.response.CrcpResponse
import com.devmon.crcp.data.network.response.ScreeningResponse
import com.devmon.crcp.data.network.response.SurveyLoadResponse
import com.devmon.crcp.data.network.service.ScreeningService
import com.devmon.crcp.domain.repository.ScreeningRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ScreeningRepositoryImpl(
    private val screeningService: ScreeningService,
) : ScreeningRepository {

    override fun load(said: Int): Flow<CrcpResponse<SurveyLoadResponse>> {
        return flow {
            emit(screeningService.loadSurvey(said))
        }
    }

    override fun save(screening: ScreeningRequest): Flow<CrcpResponse<String>> {
        return flow {
            emit(screeningService.saveSurvey(screening))
        }
    }

    override fun getScreening(sid: Int): Flow<CrcpResponse<ScreeningResponse>> {
        return flow {
            val screeningResponse = screeningService.screening(sid)

            if (screeningResponse.info == null) {
                emit(CrcpResponse("201", 0, screeningResponse, "Screening 설문지가 없습니다."))
            } else {
                emit(CrcpResponse("200", 1, screeningResponse, null))
            }
        }
    }
}