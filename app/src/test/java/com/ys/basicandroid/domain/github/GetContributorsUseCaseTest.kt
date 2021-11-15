package com.ys.basicandroid.domain.github

import com.ys.basicandroid.MainCoroutineRule
import com.ys.basicandroid.data.repository.GithubRepository
import com.ys.basicandroid.domain.Result
import com.ys.basicandroid.domain.github.GetContributorsUseCase.Param
import com.ys.basicandroid.shared.model.User
import kotlinx.coroutines.runBlocking

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetContributorsUseCaseTest {

    @get:Rule
    var coroutineRule = MainCoroutineRule()

    private val repository : GithubRepository = mock()

    @Test
    fun successExecute() = runBlocking {

        val params = Param(owner = "ys", name = "BasicAndroidSample", 1)

        // given
        val useCase = GetContributorsUseCase(repository, coroutineRule.testDispatcher)
        whenever(repository.getContributors(params.owner, params.name, params.pageNo))
            .thenReturn(
                listOf(
                    User(
                        name = "Albert Lee",
                        photoUrl = ""
                    ),
                    User(
                        name = "Hello World",
                        photoUrl = ""
                    )
                )
            )

        // when
        val result = useCase.invoke(params)

        // then
        assertTrue(result is Result.Success)

        val successResult  = result as Result.Success
        assertEquals(2, successResult.data.size)

        assertEquals("Albert Lee", successResult.data[0].name)
        assertEquals("Hello World", successResult.data[1].name)
    }

    @Test
    fun errorExecute() = runBlocking {

        val params = Param(owner = "ys", name = "BasicAndroidSample", 1)

        // given
        val useCase = GetContributorsUseCase(repository, coroutineRule.testDispatcher)
        whenever(repository.getContributors(params.owner, params.name, params.pageNo))
            .thenThrow(IllegalStateException("Test"))

        // when
        val result = useCase.invoke(params)

        // then
        assertTrue(result is Result.Error)

        val errorResult  = result as Result.Error
        assertEquals("Test", errorResult.exception.message)
    }
}