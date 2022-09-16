package com.ys.basicandroid.domain.github

import com.ys.basicandroid.data.repository.GithubRepository
import com.ys.basicandroid.domain.UseCase
import com.ys.basicandroid.common.IoDispatcher
import com.ys.basicandroid.common.model.User
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

class GetContributorsUseCase @Inject constructor(
    private val githubRepository: GithubRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<GetContributorsUseCase.Param, List<User>>(dispatcher) {

    override suspend fun execute(param: Param): List<User> {
        return githubRepository.getContributors(
            owner = param.owner,
            name = param.name,
            pageNo = param.pageNo
        )
    }

    data class Param(
        val owner: String,
        val name: String,
        val pageNo: Int
    )
}