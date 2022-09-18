package com.ys.basicandroid.presentation.ui.search.main.view

import android.content.IntentSender
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.ys.basicandroid.R
import com.ys.basicandroid.common.log.L
import com.ys.basicandroid.databinding.FragmentSearchMainBinding
import com.ys.basicandroid.domain.entity.ActionEntity
import com.ys.basicandroid.domain.entity.ClickEntity
import com.ys.basicandroid.domain.model.BookInfoItemViewModel
import com.ys.basicandroid.presentation.ui.search.main.adapter.simple.SearchMainAdapter
import com.ys.basicandroid.presentation.ui.search.main.event.SearchMainClickEntity
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

	private val oneTapClient: SignInClient by lazy {
		Identity.getSignInClient(requireActivity())
	}

	private val signInRequest: BeginSignInRequest by lazy {
		BeginSignInRequest.builder()
			.setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
				.setSupported(true)
				.build()
			)
			.setGoogleIdTokenRequestOptions(
				BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
					.setSupported(true)
					// Your server's client ID, not your Android client ID.
					.setServerClientId(getString(R.string.google_web_client_id))
					// Only show accounts previously used to sign in.
					.setFilterByAuthorizedAccounts(true)
					.build()
			)
			// Automatically sign in when exactly one credential is retrieved.
			.setAutoSelectEnabled(true)
			.build()
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {

		binding = FragmentSearchMainBinding.inflate(layoutInflater).apply {
			lifecycleOwner = viewLifecycleOwner
			viewModel = this@SearchMainFragment.searchViewModel
		}

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setBind()
		initObserve()
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

	private fun beginSignIn() {
		oneTapClient.beginSignIn(signInRequest)
			.addOnSuccessListener(requireActivity()) { result ->
				try {

					val intentSenderRequest: IntentSenderRequest =
						IntentSenderRequest
							.Builder(result.pendingIntent.intentSender)
							.build()

					signInLauncher.launch(intentSenderRequest)
				} catch (e: IntentSender.SendIntentException) {
					L.e("Couldn't start One Tap UI: ${e.localizedMessage}")
				}
			}
			.addOnFailureListener(requireActivity()) { e ->
				// No saved credentials found. Launch the One Tap sign-up flow, or
				// do nothing and continue presenting the signed-out UI.
				L.d(e.localizedMessage)
			}
	}

	private fun signOut() {
		oneTapClient.signOut()
			.addOnSuccessListener {
				searchViewModel.removeUserInfo()
			}
			.addOnFailureListener(requireActivity()) { e ->
				// No saved credentials found. Launch the One Tap sign-up flow, or
				// do nothing and continue presenting the signed-out UI.
				L.d(e.localizedMessage)
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

			is SearchMainClickEntity.GoogleLogin -> {
				if (entity.isLoggedIn) {
					signOut()
				} else {
					beginSignIn()
				}

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

	private var showOneTapUI = true

	private val signInLauncher =
		registerForActivityResult(
			ActivityResultContracts.StartIntentSenderForResult()
		) { result: ActivityResult ->
			try {
				val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
				val idToken = credential.googleIdToken
				val username = credential.id
				val password = credential.password

				searchViewModel.saveUserInfo(
					idToken = idToken.orEmpty(),
					username = username,
					password = password.orEmpty()
				)

				when {
					idToken != null -> {
						// Got an ID token from Google. Use it to authenticate
						// with your backend.
						L.d("Got ID token: $idToken")
					}
					username != null -> {
						L.d("Got username: $username")
					}
					password != null -> {
						// Got a saved username and password. Use them to authenticate
						// with your backend.
						L.d("Got password: $password")
					}
					else -> {
						// Shouldn't happen.
						L.d("No ID token or password!")
					}
				}
			} catch (e: ApiException) {
				when (e.statusCode) {
					CommonStatusCodes.CANCELED -> {
						L.d("One-tap dialog was closed.")
						// Don't re-prompt the user.
						showOneTapUI = false
					}
					CommonStatusCodes.NETWORK_ERROR -> {
						L.d("One-tap encountered a network error.")
						// Try again or just ignore.
					}
					else -> {
						L.d(
							"Couldn't get credential from result." +
								" (${e.localizedMessage})"
						)
					}
				}
			}
		}
}