package com.ys.basicandroid.presentaion.ui.search.main

import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.ys.basicandroid.R
import com.ys.basicandroid.databinding.FragmentSearchMainBinding
import com.ys.basicandroid.domain.entity.ActionEntity
import com.ys.basicandroid.domain.entity.ClickEntity
import com.ys.basicandroid.presentaion.base.ui.BaseFragment
import com.ys.basicandroid.presentaion.ui.search.main.adapter.multitype.SearchMainMultiTypeAdapter
import com.ys.basicandroid.presentaion.ui.search.main.adapter.simple.SearchMainAdapter
import com.ys.basicandroid.presentaion.ui.search.main.event.SearchMainClickEntity.ClickTitle
import com.ys.basicandroid.presentaion.ui.search.main.viewmodel.SearchMainViewModel
import com.ys.basicandroid.utils.decoration.DividerItemDecoration
import com.ys.basicandroid.utils.ext.clearItemDecoration
import com.ys.basicandroid.utils.ext.hideKeyboard
import com.ys.basicandroid.utils.ext.observeHandledEvent
import com.ys.basicandroid.utils.ext.showToast
import com.ys.basicandroid.utils.recyclerview.EndlessRecyclerScrollListener
import dagger.hilt.android.AndroidEntryPoint
import java.net.UnknownHostException
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
            val direction = SearchMainFragmentDirections.actionSearchMainFragmentToBookDetailFragment(bookInfo)
            findNavController().navigate(direction)
        }
    }

	private val searchMultiTypeAdapter: SearchMainMultiTypeAdapter by lazy {
		SearchMainMultiTypeAdapter { bookInfo ->
			val direction = SearchMainFragmentDirections.actionSearchMainFragmentToBookDetailFragment(bookInfo)
			findNavController().navigate(direction)
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

	override fun initObserve() {
		searchViewModel.books.observe(viewLifecycleOwner) { books ->
			searchAdapter.addItems(books)
		}

		observeEventNotifier()
	}

	private fun observeEventNotifier() {
		observeHandledEvent(searchViewModel.event.click) {
			handleSelectEvent(it)
		}
		observeHandledEvent(searchViewModel.event.action) {
			handleActionEvent(it)
		}
		observeHandledEvent(searchViewModel.event.throwable) {
			if (it.first is UnknownHostException) {
				showToast(getString(R.string.error_default))
			}
		}
	}

	private fun handleActionEvent(entity: ActionEntity) {
		when (entity) {}
	}

	private fun handleSelectEvent(entity: ClickEntity) {
		when (entity) {
			is ClickTitle -> {
				showToast("타이틀 클릭 테스트")
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