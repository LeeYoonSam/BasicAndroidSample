package com.ys.basicandroid.presentation.ui.search.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.ys.basicandroid.R
import com.ys.basicandroid.databinding.FragmentSearchMainBinding
import com.ys.basicandroid.domain.entity.ActionEntity
import com.ys.basicandroid.domain.entity.ClickEntity
import com.ys.basicandroid.domain.model.BookInfoItemViewModel
import com.ys.basicandroid.presentation.ui.search.main.adapter.simple.SearchMainAdapter
import com.ys.basicandroid.presentation.ui.search.main.event.SearchMainClickEntity.ClickBookInfo
import com.ys.basicandroid.presentation.ui.search.main.viewmodel.SearchMainViewModel
import com.ys.basicandroid.utils.decoration.DividerItemDecoration
import com.ys.basicandroid.utils.extensions.clearItemDecoration
import com.ys.basicandroid.utils.extensions.getAutoHideKeyboardFocusChangeListener
import com.ys.basicandroid.utils.extensions.observeHandledEvent
import com.ys.basicandroid.utils.extensions.showToast
import com.ys.basicandroid.utils.recyclerview.EndlessRecyclerScrollListener
import dagger.hilt.android.AndroidEntryPoint
import java.net.UnknownHostException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchMainFragment : Fragment() {

	private lateinit var binding: FragmentSearchMainBinding

    private val searchViewModel by viewModels<SearchMainViewModel>()

    private val searchAdapter = SearchMainAdapter()
    // private val searchAdapter = SearchMainMultiTypeAdapter()

    private val endlessRecyclerScrollListener = EndlessRecyclerScrollListener {
        searchViewModel.requestSearchBooks(true)
    }

    private var previousSearchText = ""

    private var debounceJob: Job? = null
    var isClickSearch = false

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {

		binding = FragmentSearchMainBinding.inflate(layoutInflater).apply {
			lifecycleOwner = viewLifecycleOwner
			viewModel = this@SearchMainFragment.searchViewModel
		}

		setBind()
		initObserve()

		return binding.root
	}

    private fun setBind() {
        binding.apply {

            viewModel = searchViewModel

	        etSearch.run {
		        setWatcher()
		        searchBooks { query ->
			        isClickSearch = true

			        clearAndSearch(query)
		        }

		        onFocusChangeListener = getAutoHideKeyboardFocusChangeListener()
	        }

            rvBooks.run {
                clearItemDecoration()

                adapter = searchAdapter
                addItemDecoration(DividerItemDecoration(requireContext()))
                addOnScrollListener(scrollListener)
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

    private val scrollListener = object : OnScrollListener() {
        override fun onScrollStateChanged(
            recyclerView: RecyclerView,
            newState: Int
        ) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                binding.etSearch.clearFocus()
            }
        }
    }

	private fun initObserve() {
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
			is ClickBookInfo -> {
				moveBookInfoDetail(entity.bookInfoItemViewModel)
			}
		}
	}

    private fun showToast(message: String) {
        requireContext().showToast(message)
    }

	private fun moveBookInfoDetail(bookInfoItemViewModel: BookInfoItemViewModel) {
		val direction = SearchMainFragmentDirections.actionSearchMainFragmentToBookDetailFragment(bookInfoItemViewModel)
		findNavController().navigate(direction)
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

	        lifecycleScope.launch {

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