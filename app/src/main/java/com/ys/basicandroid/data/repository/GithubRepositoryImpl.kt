package com.ys.basicandroid.data.repository

import com.ys.basicandroid.data.api.GithubApi
import com.ys.basicandroid.shared.model.User
import javax.inject.Inject

class GithubRepositoryImpl @Inject constructor(
    private val githubApi: GithubApi
) : GithubRepository {

    override suspend fun getContributors(
        owner: String,
        name: String,
        pageNo: Int
    ): List<User> {
        return githubApi.getContributors(
            owner = owner,
            name = name,
            page = pageNo
        ).map {
            User(
                name = it.name,
                photoUrl = it.photoUrl
            )
        }
    }
}