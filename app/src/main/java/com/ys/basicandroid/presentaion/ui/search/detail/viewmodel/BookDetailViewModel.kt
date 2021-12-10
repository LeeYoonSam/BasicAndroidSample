package com.ys.basicandroid.presentaion.ui.search.detail.viewmodel

import androidx.databinding.ObservableField
import com.ys.basicandroid.domain.model.BookInfo
import com.ys.basicandroid.presentaion.base.ui.BaseViewModel

class BookDetailViewModel : BaseViewModel() {

    val bookInfo =  ObservableField<BookInfo>()

    fun setBookInfo(bookInfo: BookInfo) {
        this.bookInfo.set(bookInfo)
    }

    fun onClickLike() {
        bookInfo.get()?.isLike?.let {
            if (it.get()) {
                likeOff()
            } else {
                likeOn()
            }
        }
    }

    private fun likeOn() {
        bookInfo.get()?.isLike?.set(true)
    }

    private fun likeOff() {
        bookInfo.get()?.isLike?.set(false)
    }
}