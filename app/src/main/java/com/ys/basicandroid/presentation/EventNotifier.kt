package com.ys.basicandroid.presentation

import com.ys.basicandroid.domain.entity.ActionEntity
import com.ys.basicandroid.domain.entity.ClickEntity

interface EventNotifier

/**
 * action event 전송을 위한 인터페이스
 */
interface ActionEventNotifier : EventNotifier {
	fun notifyActionEvent(entity: ActionEntity)
}

/**
 * click event 전송을 위한 인터페이스
 */
interface ClickEventNotifier : EventNotifier {
	fun notifyClickEvent(entity: ClickEntity)
}

interface ClickActionEventNotifier : ClickEventNotifier, ActionEventNotifier