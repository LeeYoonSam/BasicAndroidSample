package com.ys.basicandroid.presentation.base.ui

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.ys.basicandroid.domain.entity.ActionEntity
import com.ys.basicandroid.domain.entity.ClickEntity
import com.ys.basicandroid.presentation.ClickActionEventNotifier
import com.ys.basicandroid.presentation.event.BaseEvent
import com.ys.basicandroid.presentation.event.IBaseEvent

abstract class BaseViewModel : ViewModel(), ClickActionEventNotifier {

	protected open val _event: BaseEvent = BaseEvent()
	open val event: IBaseEvent
		get() = _event

	val isLoading = ObservableBoolean(false)

	protected open fun handleActionEvent(entity: ActionEntity) {}

	override fun notifyActionEvent(entity: ActionEntity) {
		handleActionEvent(entity)
		_event._action.setHandledValue(entity)
	}

	protected open fun handleClickEvent(entity: ClickEntity) {}
	override fun notifyClickEvent(entity: ClickEntity) {
		handleClickEvent(entity)
		_event._click.setHandledValue(entity)
	}

	override fun onCleared() {
		super.onCleared()

		hideLoading()
	}

	fun showLoading() {
		isLoading.set(true)
	}

	fun hideLoading() {
		isLoading.set(false)
	}
}