package com.devmon.crcp.di

import android.content.SharedPreferences
import com.devmon.crcp.data.datasource.MemberDataSource
import com.devmon.crcp.data.datasource.MemberDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    fun provideMemberDataSource(sharedPreferences: SharedPreferences): MemberDataSource {
        return MemberDataSourceImpl(sharedPreferences)
    }
}