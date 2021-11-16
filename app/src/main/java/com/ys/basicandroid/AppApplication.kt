package com.ys.basicandroid

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)
        Timber.plant(Timber.DebugTree())
    }
}