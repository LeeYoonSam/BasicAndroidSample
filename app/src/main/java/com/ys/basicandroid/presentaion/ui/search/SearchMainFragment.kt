package com.ys.basicandroid.presentaion.ui.search

import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.ys.basicandroid.R
import com.ys.basicandroid.databinding.FragmentSearchMainBinding
import com.ys.basicandroid.presentaion.base.ui.BaseFragment
import com.ys.basicandroid.presentaion.ui.search.adapter.SearchMainAdapter
import com.ys.basicandroid.presentaion.ui.search.viewmodel.SearchMainViewModel
import com.ys.basicandroid.utils.decoration.DividerItemDecoration
import com.ys.basicandroid.utils.ext.clearItemDecoration
import com.ys.basicandroid.utils.ext.hideKeyboard
import com.ys.basicandroid.utils.ext.showToast
import com.ys.basicandroid.utils.recyclerview.EndlessRecyclerScrollListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchMainFragment : BaseFragment<FragmentSearchMainBinding>(R.layout.fragment_search_main) {

    private val searchViewModel by viewModels<SearchMainViewModel>()

    private val searchAdapter: SearchMainAdapter by lazy {
        SearchMainAdapter { bookInfo ->
            showToast("책 클릭시 상세화면 연결")
        }
    }

    private val endlessRecyclerScrollListener = EndlessRecyclerScrollListener {
        searchViewModel.requestSearchBooks(true)
    }

    private var previousSearchText = ""

    private var debounceJob: Job? = null
    var isClickSearch = false

    override fun setBind() {
        binding.apply {

            viewModel = searchViewModel

            etSearch.setWatcher()
            etSearch.searchBooks { query ->
                isClickSearch = true

                clearAndSearch(query)
            }

            rvBooks.run {
                clearItemDecoration()

                adapter = searchAdapter
                addItemDecoration(DividerItemDecoration(requireContext()))
                addOnScrollListener(onScrollListener())
                addOnScrollListener(endlessRecyclerScrollListener)
            }
        }
    }

    private fun clearAndSearch(query: String) {
        debounceJob?.cancel()

        searchViewModel.checkSameQuery(query) {
            previousSearchText = ""
            clearBooks()

            searchViewModel.onClickSearchBooks(query)
        }
    }

    private fun onScrollListener() = object : OnScrollListener() {
        override fun onScrollStateChanged(
            recyclerView: RecyclerView,
            newState: Int
        ) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                binding.etSearch.hideKeyboard()
            }
        }
    }

    override fun setObserve() {
        searchViewModel.books.observe(viewLifecycleOwner) { books ->
            searchAdapter.addBooks(books)
        }

        searchViewModel.run {
            books.observe(viewLifecycleOwner) { books ->
                searchAdapter.addBooks(books)
            }

            error.observe(viewLifecycleOwner) { errorMessage ->
                showToast(errorMessage)
            }
        }
    }

    private fun showToast(message: String) {
        requireContext().showToast(message)
    }

    /**
     * 검색어 입력시 1초 딜레이 후 자동으로 검색
     */
    private fun EditText.setWatcher() {
        doAfterTextChanged {
            isClickSearch = false

            val searchText = it.toString().trim()
            if (searchText == previousSearchText)
                return@doAfterTextChanged

            previousSearchText = searchText

            CoroutineScope(Dispatchers.Main).launch {

                delay(1000)  //debounce timeOut
                if (searchText != previousSearchText) {
                    debounceJob?.cancel()

                    return@launch
                }

                if (!isClickSearch) {
                    debounceJob = launch(Dispatchers.Main) {
                        clearAndSearch(previousSearchText)
                    }
                }
            }
        }
    }

    /**
     * 키보드 검색 버튼 클릭
     */
    private inline fun EditText.searchBooks(crossinline callback: (query: String) -> Unit) {
        setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                val query = view.text.toString()

                if (query.isBlank()) {
                    showToast(resources.getString(R.string.hint_search))
                    return@setOnEditorActionListener true
                }

                view.hideKeyboard()
                callback.invoke(query)

                true
            }
            false
        }
    }

    private fun clearBooks() {
        endlessRecyclerScrollListener.refresh()
        searchAdapter.clear()
    }
}