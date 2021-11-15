package com.ys.basicandroid.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.ys.basicandroid.data.api.GithubApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideConvert(): Converter.Factory {
        return Json {
            ignoreUnknownKeys = true
        }.asConverterFactory("application/json".toMediaType())
    }

    @Provides
    @Singleton
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

    // @Provides
    // @Singleton
    // fun provideRetrofit(
    //     okHttpClient: OkHttpClient,
    //     converterFactory: Converter.Factory
    // ): Retrofit = Retrofit.Builder()
    //     .baseUrl("https://raw.githubusercontent.com/")
    //     .addConverterFactory(converterFactory)
    //     .client(okHttpClient)
    //     .build()
    //
    // @Provides
    // @Singleton
    // fun provideGithubApi(
    //     retrofit: Retrofit
    // ): GithubApi {
    //     return retrofit.create(GithubApi::class.java)
    // }
}