package com.devmon.crcp.data.network.service

import com.devmon.crcp.data.network.request.QnaRequest
import com.devmon.crcp.data.network.response.BaseResponse
import com.devmon.crcp.data.network.response.ConsentResponse
import com.devmon.crcp.data.network.response.ConsentStdgrpResponse
import com.devmon.crcp.data.network.response.CrcpResponse
import com.devmon.crcp.data.network.response.DefaultResponse
import com.devmon.crcp.data.network.response.MyStudyResponse
import com.devmon.crcp.data.network.response.QnaResponse
import com.devmon.crcp.data.network.response.RecruitingResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

interface StudyService {

    @POST("study/all-recruiting")
    suspend fun allRecruiting() : CrcpResponse<List<RecruitingResponse>>

    @FormUrlEncoded
    @POST("study/detail")
    suspend fun detail(
        @Field("sid") sid: Int,
    ) : BaseResponse

    @FormUrlEncoded
    @POST("study/add-favorite")
    suspend fun addFavorite(
        @Field("sid") sid: Int,
        @Field("applid") applid: Int,
    ) : BaseResponse

    @FormUrlEncoded
    @POST("study/delete-favorite")
    suspend fun deleteFavorite(
        @Field("sid") sid: Int,
        @Field("applid") applid: Int,
    ) : BaseResponse

    @FormUrlEncoded
    @POST("study/enroll/check")
    suspend fun enrollCheck(
        @Field("sid") sid: Int,
        @Field("applid") applid: Int,
    ) : BaseResponse

    @FormUrlEncoded
    @POST("study/enroll/apply")
    suspend fun enrollApply(
        @Field("sid") sid: Int,
        @Field("applid") applid: Int,
    ) : DefaultResponse<String>

    @FormUrlEncoded
    @POST("study/enroll/cancle")
    suspend fun enrollCancel(
        @Field("sid") sid: Int,
        @Field("applid") applid: Int,
        @Field("said") said: Int,
    ) : CrcpResponse<String>

    /**
     * Study - 연구 자원자 동의정보 보기
     */
    @FormUrlEncoded
    @POST("study/consent-appl")
    suspend fun consentAppl(
        @Field("csid") csid: Int,
    ) : BaseResponse

    @FormUrlEncoded
    @POST("study/qna/{said}")
    suspend fun getQnAs(
        @Path("said") said: Int,
        @Field("said") saidd: Int,
        @Field("sid") sid: Int,
    ) : CrcpResponse<List<QnaResponse>>

    @POST("study/qna")
    suspend fun pushQnA(
        @Body qnaRequest: QnaRequest,
    ) : CrcpResponse<String>

    /**
     * Study - 연구 동의서 동의철회/신청취소
     */
    @FormUrlEncoded
    @POST("study/const-withdraw")
    suspend fun constWithdraw(
        @Field("applid") applid: Int,
        @Path("said") said: Int,
    ) : BaseResponse

    /**
     * Study - 연구 동의서 법적대리인 서명 저장
     */
    @FormUrlEncoded
    @POST("study/const-lar")
    suspend fun constLar(
        @Path("said") said: Int,
        @Path("consentid") consentid: Int,
        @Path("csname") csname: String,
        @Path("cssign") cssign: String,
        @Path("signdtc") signdtc: String,
    ) : BaseResponse

    /**
     * Study - 연구 동의서 자원자 서명 저장
     */
    @FormUrlEncoded
    @POST("study/const-appl")
    suspend fun constAppl(
        @Field("said") said: Int,
        @Field("consentid") consentid: Int,
        @Field("csname") csname: String,
        @Field("cssign") cssign: String,
        @Field("signdtc") signdtc: String,
    ) : CrcpResponse<String>

    /**
     * Study - 연구 동의서 동의 시작
     */
    @FormUrlEncoded
    @POST("study/const-start")
    suspend fun constStart(
        @Field("said") said: Int,
        @Field("consentid") consentid: Int,
    ) : CrcpResponse<String>

    @FormUrlEncoded
    @POST("mystudy/all-consent")
    suspend fun allConsent(
        @Field("said") said: Int
    ): CrcpResponse<List<ConsentResponse>>

    @FormUrlEncoded
    @POST("mystudy")
    suspend fun myStudy(
        @Field("applid") applid: Int
    ): CrcpResponse<MyStudyResponse>

    @FormUrlEncoded
    @POST("mystudy/consent-stdgrp")
    suspend fun consentStdgrp(
        @Field("sid") sid: Int,
        @Field("said") said: Int,
    ): CrcpResponse<List<ConsentStdgrpResponse>>
}