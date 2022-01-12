package com.devmon.crcp.data.repository

import com.devmon.crcp.data.network.request.QnaRequest
import com.devmon.crcp.data.network.response.ConsentResponse
import com.devmon.crcp.data.network.response.ConsentStdgrpResponse
import com.devmon.crcp.data.network.response.CrcpResponse
import com.devmon.crcp.data.network.response.DefaultResponse
import com.devmon.crcp.data.network.response.MyStudyResponse
import com.devmon.crcp.data.network.response.QnaResponse
import com.devmon.crcp.data.network.response.RecruitingResponse
import com.devmon.crcp.data.network.service.StudyService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

interface StudyRepository {
    suspend fun allRecruiting() : Flow<CrcpResponse<List<RecruitingResponse>>>
//    suspend fun getStudy(sid: String): Flow<CrcpResponse<StudyDetailResponse>>

    suspend fun enrollApply(sid: Int, applid: Int) : DefaultResponse<String>
    suspend fun enrollCancel(sid: Int, applid: Int, said: Int) : CrcpResponse<String>

    suspend fun myStudy(applid: Int) : Flow<CrcpResponse<MyStudyResponse>>

    suspend fun allConsent(said: Int) : Flow<CrcpResponse<List<ConsentResponse>>>
    suspend fun consentStdgrp(sid: Int, said: Int) : Flow<CrcpResponse<List<ConsentStdgrpResponse>>>
    suspend fun constAppl(said: Int, consentId: Int, name: String, sign: String, currentDate: String): Flow<CrcpResponse<String>>
    suspend fun constStart(said: Int, consentId: Int): Flow<CrcpResponse<String>>

    suspend fun getQnaList(said: Int, sid: Int): Flow<CrcpResponse<List<QnaResponse>>>
    suspend fun sendQna(qnaRequest: QnaRequest): Flow<CrcpResponse<String>>
}

class StudyRepositoryImpl(
    private val studyService: StudyService,
) : StudyRepository {

    override suspend fun allRecruiting(): Flow<CrcpResponse<List<RecruitingResponse>>> {
        return flowOf(studyService.allRecruiting())
    }

//    override suspend fun getStudy(sid: String): Flow<CrcpResponse<StudyDetailResponse>> {
//        return flowOf(studyService.detail(sid))
//    }

    override suspend fun enrollApply(sid: Int, applid: Int): DefaultResponse<String> {
        return studyService.enrollApply(sid, applid)
    }

    override suspend fun enrollCancel(sid: Int, applid: Int, said: Int): CrcpResponse<String> {
        return studyService.enrollCancel(sid, applid, said)
    }

    override suspend fun myStudy(applid: Int): Flow<CrcpResponse<MyStudyResponse>> {
        return flowOf(studyService.myStudy(applid))
    }

    override suspend fun allConsent(said: Int): Flow<CrcpResponse<List<ConsentResponse>>> {
        return flowOf(studyService.allConsent(said))
    }

    override suspend fun consentStdgrp(
        sid: Int,
        said: Int
    ): Flow<CrcpResponse<List<ConsentStdgrpResponse>>> {
        return flowOf(studyService.consentStdgrp(sid, said))
    }

    override suspend fun constAppl(
        said: Int,
        consentId: Int,
        name: String,
        sign: String,
        currentDate: String
    ): Flow<CrcpResponse<String>> {
        return flow {
            val response = studyService.constAppl(said, consentId, name, sign, currentDate)
            emit(response)
        }
    }

    override suspend fun constStart(said: Int, consentId: Int): Flow<CrcpResponse<String>> {
        return flow {
            val response = studyService.constStart(said, consentId)
            emit(response)
        }
    }

    override suspend fun getQnaList(said: Int, sid: Int): Flow<CrcpResponse<List<QnaResponse>>> {
        return flow {
            val response = studyService.getQnAs(said, said, sid)
            emit(response)
        }
    }

    override suspend fun sendQna(qnaRequest: QnaRequest): Flow<CrcpResponse<String>> {
        return flow {
            val response = studyService.pushQnA(qnaRequest)
            emit(response)
        }
    }
}