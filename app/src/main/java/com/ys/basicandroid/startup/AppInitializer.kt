package com.ys.basicandroid.startup

import android.content.Context
import androidx.startup.Initializer
import com.ys.basicandroid.common.authentication.manager.UserInfoManager

class AppInitializer : Initializer<Unit> {

	override fun create(context: Context) {
		UserInfoManager.init(context)
	}

	override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}