package com.ys.basicandroid.presentaion.ui.search.main.adapter.multitype

import com.ys.basicandroid.domain.model.BookInfoItemViewModel
import com.ys.basicandroid.presentaion.base.adapter.IViewTypeGetter

interface ISearchItemViewModel : IViewTypeGetter<SearchViewType> {

	val itemId: String

	override fun getViewType(): SearchViewType {
		return when (this) {
			is BookInfoItemViewModel -> SearchViewType.ITEM
			else -> SearchViewType.ITEM
		}
	}
}