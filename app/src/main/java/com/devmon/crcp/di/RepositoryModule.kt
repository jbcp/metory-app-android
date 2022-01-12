package com.devmon.crcp.di

import com.devmon.crcp.data.datasource.MemberDataSource
import com.devmon.crcp.data.network.service.MemberService
import com.devmon.crcp.data.network.service.PushService
import com.devmon.crcp.data.network.service.ScreeningService
import com.devmon.crcp.data.network.service.StudyService
import com.devmon.crcp.data.repository.MemberRepositoryImpl
import com.devmon.crcp.data.repository.StudyRepository
import com.devmon.crcp.data.repository.StudyRepositoryImpl
import com.devmon.crcp.domain.repository.MemberRepository
import com.devmon.crcp.domain.repository.ScreeningRepository
import com.devmon.crcp.ui.screening.ScreeningRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMemberRepository(
        pushService: PushService,
        memberService: MemberService,
        memberDataSource: MemberDataSource,
    ): MemberRepository {
        return MemberRepositoryImpl(
            pushService,
            memberService,
            memberDataSource,
        )
    }

    @Provides
    @Singleton
    fun provideStudyRepository(
        studyService: StudyService,
    ): StudyRepository {
        return StudyRepositoryImpl(
            studyService,
        )
    }

    @Singleton
    @Provides
    fun provideScreeningRepository(
        screeningService: ScreeningService,
    ): ScreeningRepository {
        return ScreeningRepositoryImpl(screeningService)
    }
}