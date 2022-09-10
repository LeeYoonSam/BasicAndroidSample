package com.ys.basicandroid.startup

import android.content.Context
import androidx.startup.Initializer
import com.ys.basicandroid.BuildConfig
import timber.log.Timber

class TimberInitializer : Initializer<Unit> {

	override fun create(context: Context) {
		if (BuildConfig.DEBUG) {
			Timber.plant(Timber.DebugTree())
		}
	}

	override fun dependencies(): List<Class<out Initializer<*>>> {
		return listOf(ThreeTenInitializer::class.java)
	}
}