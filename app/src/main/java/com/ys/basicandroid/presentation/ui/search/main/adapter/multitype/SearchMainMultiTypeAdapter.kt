package com.ys.basicandroid.presentation.ui.search.main.adapter.multitype

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.ys.basicandroid.presentation.base.adapter.BaseBindingViewHolder
import com.ys.basicandroid.presentation.base.adapter.ItemDiffCallback
import com.ys.basicandroid.presentation.base.adapter.ListBindingAdapter

// ViewType 적용 예
class SearchMainMultiTypeAdapter : ListBindingAdapter<SearchViewType, ISearchItemViewModel>(
	ItemDiffCallback(
		onItemsTheSame = { old, new ->
			old.itemId == new.itemId
		},
		onContentsTheSame = { old, new -> old == new }
	)) {

	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): BaseBindingViewHolder<ISearchItemViewModel, ViewDataBinding> {
		return SearchViewHolder.getViewHolder(
			parent,
			SearchViewType.values()[viewType]
		)
	}
}

enum class SearchViewType {
	ITEM
}