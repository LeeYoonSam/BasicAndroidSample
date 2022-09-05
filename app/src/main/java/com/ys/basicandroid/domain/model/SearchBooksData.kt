package com.ys.basicandroid.domain.model

data class SearchBooksData(
    val books: List<BookInfoItemViewModel>,
    val meta: PagingMeta
)