package com.ys.basicandroid.presentaion.ui.search.main.event

import com.ys.basicandroid.domain.entity.ClickEntity

sealed class SearchMainClickEntity : ClickEntity() {
	object ClickTitle : SearchMainClickEntity()
}