package com.devmon.crcp.di

import com.devmon.crcp.BuildConfig
import com.devmon.crcp.ui.alert.AlertFactory
import com.devmon.crcp.ui.model.Clock
import com.devmon.crcp.ui.model.LocalClock
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideClock(): Clock = LocalClock()

    @Provides
    @Singleton
    fun provideAlertFactory(): AlertFactory = AlertFactory()

    @Provides
    @Singleton
    fun provideDebug(): Boolean = BuildConfig.DEBUG
}