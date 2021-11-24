package com.ys.basicandroid.data.repository

import com.ys.basicandroid.domain.book.SearchBooksInfoUseCase.Params
import com.ys.basicandroid.domain.model.SearchBooksData

interface SearchRepository {
    suspend fun searchBooks(param: Params): SearchBooksData
}