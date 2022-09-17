package com.ys.basicandroid.domain.book

import com.ys.basicandroid.domain.repository.SearchRepository
import com.ys.basicandroid.domain.UseCase
import com.ys.basicandroid.domain.book.SearchBooksInfoUseCase.Params
import com.ys.basicandroid.domain.model.SearchBooksData
import com.ys.basicandroid.presentation.ClickEventNotifier
import com.ys.basicandroid.common.IoDispatcher
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

class SearchBooksInfoUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<Params, SearchBooksData>(dispatcher) {

    override suspend fun execute(param: Params): SearchBooksData {
        return searchRepository.searchBooks(param)
    }

    data class Params(
        val query: String,
        val page: Int = 1,
	    val clickEventNotifier: ClickEventNotifier
    )
}