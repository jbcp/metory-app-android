package com.devmon.crcp.data.repository

import com.devmon.crcp.data.datasource.MemberDataSource
import com.devmon.crcp.data.network.request.ChangePasswordRequest
import com.devmon.crcp.data.network.request.LoginRequest
import com.devmon.crcp.data.network.request.RegisterRequest
import com.devmon.crcp.data.network.request.UserJoinRequest
import com.devmon.crcp.data.network.response.ApiInfoResponse
import com.devmon.crcp.data.network.response.BaseResponse
import com.devmon.crcp.data.network.response.CrcpResponse
import com.devmon.crcp.data.network.response.UserResponse
import com.devmon.crcp.data.network.response.toUi
import com.devmon.crcp.data.network.service.MemberService
import com.devmon.crcp.data.network.service.PushService
import com.devmon.crcp.domain.model.User
import com.devmon.crcp.domain.repository.MemberRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class MemberRepositoryImpl(
    private val pushService: PushService,
    private val memberService: MemberService,
    private val memberDataSource: MemberDataSource,
) : MemberRepository {

    override fun requestRegister(userInfo: RegisterRequest): Flow<CrcpResponse<String>> {
        return flow {
            emit(memberService.requestRegister(userInfo = userInfo))
        }.flowOn(Dispatchers.IO)
    }

    override fun userJoin(userJoinRequest: UserJoinRequest): Flow<CrcpResponse<String>> {
        return flow {
            emit(memberService.userJoin(userJoinRequest))
        }.flowOn(Dispatchers.IO)
    }

    override fun requestLogin(loginRequest: LoginRequest): Flow<CrcpResponse<UserResponse>> {
        return flow {
            emit(memberService.requestLogin(loginRequest = loginRequest))
        }.flowOn(Dispatchers.IO)
    }

    override fun setAutoLogin(isAutoLogin: Boolean) = memberDataSource.setAutoLogin(isAutoLogin)

    override fun getAutoLogin(): Boolean = memberDataSource.getAutoLogin()

    override fun setUserInfo(userResponse: UserResponse?) =
        memberDataSource.setUserInfo(userResponse)

    override fun changePassword(changePasswordRequest: ChangePasswordRequest): Flow<BaseResponse> {
        return flow {
            emit(memberService.changePassword(changePasswordRequest = changePasswordRequest))
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getUserInfo(): UserResponse? = withContext(Dispatchers.IO) {
        memberDataSource.getUserInfo()
    }

    override suspend fun detail(applid: Int): User {
        val resp = memberService.detail(applid)
        if (resp.isSuccessful()) {
            val user = resp.data?.firstOrNull()
            return user?.toUi() ?: User.EMPTY
        }
        return User.EMPTY
    }

    override suspend fun getRegisterTerms(): Flow<CrcpResponse<String>> {
        return flowOf(memberService.getRegisterTerms())
    }

    override suspend fun updateUserInfo(
        applid: String,
        applpwd: String,
        applsex: Int,
        applphonenum: String,
        applbrthdtc: String,
    ): Flow<CrcpResponse<String>> {
        return flowOf(
            memberService.updateUserInfo(
                applid,
                applpwd,
                applsex,
                applphonenum,
                applbrthdtc,
            )
        )
    }

    override suspend fun getPassword(): String = withContext(Dispatchers.IO) {
        memberDataSource.getPassword()
    }

    override suspend fun setPassword(password: String) = withContext(Dispatchers.IO) {
        memberDataSource.setPassword(password)
    }

    override suspend fun sendPushToken(
        email: String,
        token: String,
    ): Flow<CrcpResponse<String>> {
        return flowOf(pushService.updatePushToken(email, token, 0))
    }

    override suspend fun getApiInfo(): Flow<CrcpResponse<ApiInfoResponse>> {
        return flow {
            emit(memberService.getApiInfo())
        }
    }

    override fun saveSiteIp(ip: String) {
        memberDataSource.saveSiteIp(ip)
    }

    override fun getSiteIp(): String {
        return memberDataSource.getSiteIp()
    }

    override fun saveSubjectIp(ip: String) {
        memberDataSource.saveSubjectIp(ip)
    }

    override fun getSubjectIp(): String {
        return memberDataSource.getSubjectIp()
    }
}