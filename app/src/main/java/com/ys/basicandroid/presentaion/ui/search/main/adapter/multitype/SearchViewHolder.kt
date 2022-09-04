package com.ys.basicandroid.presentaion.ui.search.main.adapter.multitype

import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.ys.basicandroid.presentaion.base.adapter.BaseBindingViewHolder
import com.ys.basicandroid.presentaion.ui.search.main.adapter.multitype.SearchMainMultiTypeAdapter.ItemHandler

abstract class SearchViewHolder<VM : ISearchItemViewModel, B : ViewDataBinding>(
	itemView: View
) : BaseBindingViewHolder<VM, B>(itemView) {

	companion object {
		@Suppress("UNCHECKED_CAST")
		fun getViewHolder(
			parent: ViewGroup,
			viewType: SearchViewType,
			itemHandler: ItemHandler
		): SearchViewHolder<ISearchItemViewModel, ViewDataBinding> {
			return when (viewType) {
				SearchViewType.ITEM -> SearchItemViewHolder.newInstance(parent, itemHandler)
			} as SearchViewHolder<ISearchItemViewModel, ViewDataBinding>
		}
	}
}