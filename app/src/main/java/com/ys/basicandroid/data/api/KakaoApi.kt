package com.ys.basicandroid.data.api

import com.ys.basicandroid.data.model.SearchBookResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoApi {
    @GET("/v3/search/book")
    suspend fun searchBooks(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("size") size: Int = 50
    ): SearchBookResponse
}