package com.ys.basicandroid.presentaion.ui.search.main.adapter.multitype

import android.view.View
import android.view.ViewGroup
import com.ys.basicandroid.R.layout
import com.ys.basicandroid.databinding.ItemBookMultiTypeBinding
import com.ys.basicandroid.domain.model.BookInfo
import com.ys.basicandroid.presentaion.ui.search.main.adapter.multitype.SearchMainMultiTypeAdapter.ItemHandler
import com.ys.basicandroid.utils.ext.createView

class SearchItemViewHolder(
	itemView: View,
	private val itemHandler: ItemHandler
) : SearchViewHolder<BookInfo, ItemBookMultiTypeBinding>(itemView) {
    companion object {
        fun newInstance(parent: ViewGroup, itemHandler: ItemHandler) =
            SearchItemViewHolder(parent.createView(layout.item_book_multi_type), itemHandler)
    }

    override fun onBind(item: BookInfo, position: Int) {
        binding?.run {
            this.item = item
	        this.itemHandler = this@SearchItemViewHolder.itemHandler
            executePendingBindings()
        }
    }
}
