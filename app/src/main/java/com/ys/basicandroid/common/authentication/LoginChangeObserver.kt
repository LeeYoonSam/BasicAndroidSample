package com.ys.basicandroid.common.authentication

import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.ys.basicandroid.common.authentication.manager.UserInfoManager
import com.ys.basicandroid.common.log.L

class LoginChangeObserver(
    lifecycleOwner: LifecycleOwner,
    private val callback: (Boolean) -> Unit
) : LifecycleEventObserver {
    private var isPaused = false
    private var savedLogin = false
    private val isLogin: Boolean
        get() = UserInfoManager.isLoggedIn()

    init {
        lifecycleOwner.lifecycle.addObserver(this)
        savedLogin = isLogin
    }

	override fun onStateChanged(source: LifecycleOwner, event: Event) {
		when (event) {
			Event.ON_RESUME -> {
				if (isPaused && savedLogin != isLogin) {
					L.d("changed login : $isLogin")
					callback.invoke(isLogin)
				}
				isPaused = false
			}

			Event.ON_PAUSE -> {
				isPaused = true
				savedLogin = isLogin
			}

			else -> {}
		}
	}
}

fun LifecycleOwner.observeChangedLogin(callback: (Boolean) -> Unit) {
    LoginChangeObserver(this, callback)
}