package com.devmon.crcp.di

import com.devmon.crcp.BuildConfig
import com.devmon.crcp.data.network.CRCPResponseInterceptor
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @PushRetrofit
    @Provides
    fun providePushRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.PUSH_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @CrcpRetrofit
    @Provides
    fun provideCrcpRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.HOST_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    fun provideLoggingInterceptor(isDebug: Boolean): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            if (isDebug) level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    fun provideCRCPHttpClient(
        logger: HttpLoggingInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(logger)
            .addInterceptor(CRCPResponseInterceptor())
            .build()
    }

    @Provides
    fun provideGsonConverterFactory() = GsonConverterFactory.create(
        GsonBuilder().setLenient().create()
    )
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PushRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CrcpRetrofit