package com.ys.basicandroid.presentaion.ui.search.main.adapter

import com.ys.basicandroid.R
import com.ys.basicandroid.BR
import com.ys.basicandroid.domain.model.BookInfo
import com.ys.basicandroid.presentaion.base.adapter.DataBindingViewHolder
import com.ys.basicandroid.presentaion.base.adapter.ItemDiffCallback
import com.ys.basicandroid.presentaion.base.adapter.ListBindingAdapter

internal class SearchMainAdapter (
    private val itemHandler: ItemHandler
) : ListBindingAdapter<BookInfo>(
    ItemDiffCallback(
        onItemsTheSame = { old, new ->
            old.title == new.title &&
                old.contents == new.contents &&
                old.thumbnail == new.thumbnail &&
                old.datetime == new.datetime
        },
        onContentsTheSame = { old, new -> old == new }
    )) {

    fun addBooks(newBooks: List<BookInfo>) {
        submitList(newBooks)
    }

    fun clear() {
        submitList(emptyList())
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_book
    }

    override fun viewBindViewHolder(holder: DataBindingViewHolder<BookInfo>, position: Int) {
        super.viewBindViewHolder(holder, position)
        holder.binding.setVariable(BR.itemHandler, itemHandler)
    }

    fun interface ItemHandler {
        fun clickEvent(bookInfo: BookInfo)
    }
}