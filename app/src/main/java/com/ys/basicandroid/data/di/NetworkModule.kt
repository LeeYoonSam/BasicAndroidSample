package com.ys.basicandroid.data.di

import com.ys.basicandroid.BuildConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.ys.basicandroid.data.api.GithubApi
import com.ys.basicandroid.data.api.KakaoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Named("defaultHeaders")
    fun provideHeaders(): Interceptor = Interceptor {
        it.run {
            proceed(
                request().newBuilder().apply {
                    addHeader("Authorization", BuildConfig.KAKAO_REST_API_AUTHORIZATION)
                }.build()
            )
        }
    }

    @Provides
    fun provideOkHttpClient(@Named("defaultHeaders") headers: Interceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(headers)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    fun provideConvert(): Converter.Factory {
        return Json {
            ignoreUnknownKeys = true
        }.asConverterFactory("application/json".toMediaType())
    }

    @Provides
    fun provideGithubApi(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): GithubApi {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .build()
            .create(GithubApi::class.java)
    }

    @Provides
    fun provideKakaoApi(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): KakaoApi {
        return Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com")
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .build()
            .create(KakaoApi::class.java)
    }
}