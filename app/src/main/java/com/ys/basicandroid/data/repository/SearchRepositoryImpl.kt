package com.ys.basicandroid.data.repository

import com.ys.basicandroid.data.api.KakaoApi
import com.ys.basicandroid.domain.book.SearchBooksInfoUseCase.Params
import com.ys.basicandroid.domain.model.SearchBooksData
import com.ys.basicandroid.presentaion.base.ui.search.main.viewmodel.SearchViewModelMapper
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val kakaoApi: KakaoApi
): SearchRepository {
    override suspend fun searchBooks(param: Params): SearchBooksData {
        val searchBooks = kakaoApi.searchBooks(
            query = param.query,
            page = param.page
        )

        val books = SearchViewModelMapper.getBooksInfo(searchBooks.documents.orEmpty())
        val pagingMeta = SearchViewModelMapper.getPagingMeta(searchBooks.meta, param.page)

        return SearchBooksData(
            books = books,
            meta = pagingMeta
        )
    }
}