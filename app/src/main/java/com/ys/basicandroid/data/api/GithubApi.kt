package com.ys.basicandroid.data.api

import com.ys.basicandroid.data.model.GithubContributor
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {
    @GET("repos/{owner}/{name}/contributors?per_page=30")
    suspend fun getContributors(
        @Path("owner") owner: String,
        @Path("name") name: String,
        @Query("page") page: Int
    ): List<GithubContributor>
}