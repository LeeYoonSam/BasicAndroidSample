package com.ys.basicandroid.presentaion.ui.search.main.event

import com.ys.basicandroid.domain.entity.ClickEntity
import com.ys.basicandroid.domain.model.BookInfoItemViewModel

sealed class SearchMainClickEntity : ClickEntity() {
	data class ClickBookInfo(val bookInfoItemViewModel: BookInfoItemViewModel) : SearchMainClickEntity()
}