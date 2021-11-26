package com.ys.basicandroid.presentaion.ui.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ys.basicandroid.domain.Result
import com.ys.basicandroid.domain.book.SearchBooksInfoUseCase
import com.ys.basicandroid.domain.book.SearchBooksInfoUseCase.Params
import com.ys.basicandroid.domain.model.BookInfo
import com.ys.basicandroid.domain.model.PagingMeta
import com.ys.basicandroid.presentaion.base.ui.BaseViewModel
import com.ys.basicandroid.utils.ext.orFalse
import dagger.hilt.android.lifecycle.HiltViewModel
import java.net.UnknownHostException
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class SearchMainViewModel @Inject constructor(
    private val searchBooksInfoUseCase: SearchBooksInfoUseCase,
    private val stringProvider: SearchMainStringProvider
): BaseViewModel() {

    val viewState = SearchMainViewState()

    private val _books = MutableLiveData<List<BookInfo>>(emptyList())
    val books: LiveData<List<BookInfo>> = _books

    private var query: String = ""
    private var meta = PagingMeta(false)

    /**
     * UseCase 를 통해서 API 에서 검색 데이터 및 meta 정보를 전달 받음
     *
     * @param params 책 검색에 필요한 파라미터 데이터
     * @param isLoadMore 스크롤로 인한 데이터 인지 여부 - 기존 데이터에 추가해서 전달할지, 새로 전달할지를 결정하는 요소
     */
    private fun searchBooks(params: Params, isLoadMore: Boolean = false) {

        if (meta.isEnd) return

        viewModelScope.launch {
            showLoading()

            when (val result = searchBooksInfoUseCase(params)) {
                is Result.Success -> {
                    // 더보기 일때 데이터 추가
                    if (isLoadMore) {
                        val newBooks = _books.value.orEmpty()
                        _books.value = newBooks + result.data.books
                    }
                    // 첫 검색일때 데이터 교체
                    else {
                        _books.value = result.data.books
                    }

                    viewState.hasBooks.set(_books.value?.isNotEmpty().orFalse())

                    // 메타 정보 저장
                    meta = result.data.meta

                    hideLoading()
                }

                is Result.Error -> {
                    if (result.exception is UnknownHostException) {
                        _error.value = stringProvider.getString(SearchMainStringProvider.Code.ERROR_DEFAULT)
                    }

                    initSearchInfo()
                    hideLoading()
                }
            }
        }
    }

    /**
     * 검새에 필요한 기본 정보 초기화
     */
    private fun initSearchInfo() {
        query = ""
        initMetaData()
        viewState.hasBooks.set(false)
        _books.value = emptyList()
    }

    /**
     * 검색 버튼 클릭 액션
     *
     * @param query 검색 쿼리
     */
    fun onClickSearchBooks(query: String) {
        initMetaData()

        this.query = query
        requestSearchBooks(false)
    }

    /**
     * 이전에 검색했던것과 다른 쿼리일때만 [action] 을 실행
     *
     * @param newQuery 새로운 검색 쿼리
     */
    fun checkSameQuery(newQuery: String, action: () -> Unit) {
        if (query == newQuery) return

        action()
    }

    /**
     * 책 검색 요청에 필요한 데이터 지정
     *
     * @param isLoadMore 스크롤로 인한 데이터 요청인지 여부
     */
    fun requestSearchBooks(isLoadMore: Boolean) {
        searchBooks(
            params = Params(
                query = query,
                page = meta.currentPage
            ),
            isLoadMore = isLoadMore
        )
    }

    /**
     * 새로운 검색을 위해 [PagingMeta] 데이터 초기화
     */
    private fun initMetaData() {
        meta = PagingMeta(false)
    }
}