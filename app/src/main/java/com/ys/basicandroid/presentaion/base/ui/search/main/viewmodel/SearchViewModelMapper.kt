package com.ys.basicandroid.presentaion.base.ui.search.main.viewmodel

import com.ys.basicandroid.data.model.Document
import com.ys.basicandroid.data.model.Meta
import com.ys.basicandroid.domain.model.BookInfo
import com.ys.basicandroid.domain.model.PagingMeta
import com.ys.basicandroid.utils.ext.orFalse
import com.ys.basicandroid.utils.ext.orZero
import com.ys.basicandroid.utils.time.TimeUtils

object SearchViewModelMapper {
    /**
     * 책 검색 후 받은 데이터를 domain model 로 변환
     *
     * @param documents API 호출 후 전달되는 [Document] 정보
     */
    fun getBooksInfo(documents: List<Document>?) : List<BookInfo> {
        return documents
            .orEmpty()
            .mapTo(mutableListOf()) { document ->
                BookInfo(
                    authors = document.authors.orEmpty(),
                    translators = document.translators.orEmpty(),
                    contents = document.contents.orEmpty(),
                    datetime = TimeUtils.zonedDateTimeToLocalDate(document.datetime.orEmpty()),
                    price = document.price.orZero(),
                    publisher = document.publisher.orEmpty(),
                    sale_price = document.sale_price.orZero(),
                    status = document.status.orEmpty(),
                    thumbnail = document.thumbnail.orEmpty(),
                    title = document.title.orEmpty(),
                    url = document.url.orEmpty(),
                    bookLike = false
                )
            }
    }

    /**
     * 메타데이터와 페이징 정보를 담는 [PagingMeta] 정보 반환
     *
     * @param meta API 호출 후 전달되는 [Meta] 정보
     * @param page 가장 최근에 호출한 page 에서 다음 호출을 위해 1증가
     */
    fun getPagingMeta(meta: Meta?, page: Int) = PagingMeta(
        isEnd = meta?.isEnd.orFalse(),
        currentPage = page + 1
    )
}