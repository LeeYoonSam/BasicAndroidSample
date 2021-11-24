package com.ys.basicandroid.domain.model

data class SearchBooksData(
    val books: List<BookInfo>,
    val meta: PagingMeta
)