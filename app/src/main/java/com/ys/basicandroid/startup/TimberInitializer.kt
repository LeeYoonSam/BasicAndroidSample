package com.ys.basicandroid.startup

import android.content.Context
import androidx.startup.Initializer
import com.ys.basicandroid.common.log.L

class TimberInitializer : Initializer<Unit> {

	override fun create(context: Context) {
		L.init()
	}

	override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}