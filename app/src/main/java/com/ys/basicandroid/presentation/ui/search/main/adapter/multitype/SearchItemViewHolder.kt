package com.ys.basicandroid.presentation.ui.search.main.adapter.multitype

import android.view.View
import android.view.ViewGroup
import com.ys.basicandroid.R.layout
import com.ys.basicandroid.databinding.ItemBookBinding
import com.ys.basicandroid.domain.model.BookInfoItemViewModel
import com.ys.basicandroid.utils.extensions.createView

class SearchItemViewHolder(
	itemView: View,
) : SearchViewHolder<BookInfoItemViewModel, ItemBookBinding>(itemView) {
    companion object {
        fun newInstance(parent: ViewGroup) =
            SearchItemViewHolder(parent.createView(layout.item_book))
    }

    override fun onBind(item: BookInfoItemViewModel, position: Int) {
        binding?.run {
            this.item = item
            executePendingBindings()
        }
    }
}
