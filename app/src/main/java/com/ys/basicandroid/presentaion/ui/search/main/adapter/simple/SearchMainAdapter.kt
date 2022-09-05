package com.ys.basicandroid.presentaion.ui.search.main.adapter.simple

import com.ys.basicandroid.R
import com.ys.basicandroid.domain.model.BookInfoItemViewModel
import com.ys.basicandroid.presentaion.base.adapter.ItemDiffCallback
import com.ys.basicandroid.presentaion.base.adapter.SimpleListBindingAdapter

// ViewType 이 하나일때 적용 예
class SearchMainAdapter : SimpleListBindingAdapter<BookInfoItemViewModel>(
    ItemDiffCallback(
        onItemsTheSame = { old, new ->
            old.title == new.title &&
                old.contents == new.contents &&
                old.thumbnail == new.thumbnail &&
                old.datetime == new.datetime
        },
        onContentsTheSame = { old, new -> old == new }
    )) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_book
    }
}