package com.ys.basicandroid.data.repository

import com.ys.basicandroid.data.api.KakaoApi
import com.ys.basicandroid.domain.book.SearchBooksInfoUseCase.Params
import com.ys.basicandroid.domain.model.SearchBooksData
import com.ys.basicandroid.domain.repository.SearchRepository
import com.ys.basicandroid.presentation.ui.search.main.viewmodel.SearchViewModelMapper
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val kakaoApi: KakaoApi
): SearchRepository {
    override suspend fun searchBooks(param: Params): SearchBooksData {
        val searchBooks = kakaoApi.searchBooks(
            query = param.query,
            page = param.page
        )

        val books = SearchViewModelMapper.getBooksInfo(
	        documents = searchBooks.documents.orEmpty(),
	        clickEventNotifier = param.clickEventNotifier
        )
        val pagingMeta = SearchViewModelMapper.getPagingMeta(searchBooks.meta, param.page)

        return SearchBooksData(
            books = books,
            meta = pagingMeta
        )
    }
}