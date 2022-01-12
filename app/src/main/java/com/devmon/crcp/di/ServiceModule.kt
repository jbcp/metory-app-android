package com.devmon.crcp.di

import com.devmon.crcp.data.network.service.MemberService
import com.devmon.crcp.data.network.service.PushService
import com.devmon.crcp.data.network.service.ScreeningService
import com.devmon.crcp.data.network.service.StudyService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    fun provideMemberService(@CrcpRetrofit retrofit: Retrofit): MemberService =
        retrofit.create(MemberService::class.java)

    @Provides
    fun provideStudyService(@CrcpRetrofit retrofit: Retrofit): StudyService =
        retrofit.create(StudyService::class.java)

    @Provides
    fun providePushService(@PushRetrofit retrofit: Retrofit): PushService =
        retrofit.create(PushService::class.java)

    @Provides
    fun provideScreeningService(@CrcpRetrofit retrofit: Retrofit): ScreeningService =
        retrofit.create(ScreeningService::class.java)
}