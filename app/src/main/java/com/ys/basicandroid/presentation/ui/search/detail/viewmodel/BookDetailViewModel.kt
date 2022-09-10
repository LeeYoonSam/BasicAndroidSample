package com.ys.basicandroid.presentation.ui.search.detail.viewmodel

import androidx.databinding.ObservableField
import com.ys.basicandroid.domain.model.BookInfoItemViewModel
import com.ys.basicandroid.presentation.base.ui.BaseViewModel

class BookDetailViewModel : BaseViewModel() {

    val bookInfoItemViewModel =  ObservableField<BookInfoItemViewModel>()

    fun setBookInfo(bookInfoItemViewModel: BookInfoItemViewModel) {
        this.bookInfoItemViewModel.set(bookInfoItemViewModel)
    }

    fun onClickLike() {
        bookInfoItemViewModel.get()?.isLike?.let {
            if (it.get()) {
                likeOff()
            } else {
                likeOn()
            }
        }
    }

    private fun likeOn() {
        bookInfoItemViewModel.get()?.isLike?.set(true)
    }

    private fun likeOff() {
        bookInfoItemViewModel.get()?.isLike?.set(false)
    }
}