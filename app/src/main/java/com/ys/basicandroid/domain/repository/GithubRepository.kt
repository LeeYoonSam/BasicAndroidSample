package com.ys.basicandroid.domain.repository

import com.ys.basicandroid.common.model.User

interface GithubRepository {
    suspend fun getContributors(
        owner: String,
        name: String,
        pageNo: Int
    ): List<User>
}