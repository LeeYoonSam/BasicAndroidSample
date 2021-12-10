package com.ys.basicandroid.presentaion.ui.search.detail

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.ys.basicandroid.R
import com.ys.basicandroid.databinding.FragmentBookDetailBinding
import com.ys.basicandroid.presentaion.base.ui.BaseFragment
import com.ys.basicandroid.presentaion.ui.search.detail.viewmodel.BookDetailViewModel

class BookDetailFragment : BaseFragment<FragmentBookDetailBinding>(R.layout.fragment_book_detail) {
    private val bookDetailArgs by navArgs<BookDetailFragmentArgs>()
    private val bookDetailViewModel by viewModels<BookDetailViewModel>()

    override fun setInit() {
        bookDetailViewModel.setBookInfo(bookDetailArgs.bookInfo)
    }

    override fun setBind() {
        binding.run {
            viewModel = bookDetailViewModel
        }
    }
}