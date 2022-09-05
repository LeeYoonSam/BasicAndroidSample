package com.ys.basicandroid.presentaion.base.ui

import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.snackbar.Snackbar
import com.ys.basicandroid.R
import com.ys.basicandroid.domain.entity.ActionEntity
import com.ys.basicandroid.domain.entity.ClickEntity

abstract class BaseActivity<T: ViewDataBinding>(
    @LayoutRes private val layoutId: Int
) : AppCompatActivity() {

    protected lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this

	    setBind()
	    initObserve()
	    initData()
	    registerNetworkCallback()
    }

	private val internetConnectionSnackbar: Snackbar? by lazy {
		Snackbar.make(binding.root, resources.getString(R.string.error_default), Snackbar.LENGTH_INDEFINITE)
	}

	private fun registerNetworkCallback() {
		val connectivityManager = getSystemService(ConnectivityManager::class.java)
		connectivityManager?.let {
			if (VERSION.SDK_INT >= VERSION_CODES.N) {
				it.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
					override fun onAvailable(network : Network) {
						internetConnectionSnackbar?.let { snackbar ->
							if (snackbar.isShown) {
								snackbar.dismiss()
							}
						}
					}

					override fun onLost(network : Network) {
						internetConnectionSnackbar?.show()
					}

					override fun onCapabilitiesChanged(network : Network, networkCapabilities : NetworkCapabilities) {
					}

					override fun onLinkPropertiesChanged(network : Network, linkProperties : LinkProperties) {
					}
				})
			}
		}
	}

	open fun setBind() {}

	open fun initObserve() {}

	open fun initData() {}

	open fun handleSelectEvent(entity: ClickEntity) {}

	open fun handleActionEvent(entity: ActionEntity) {}
}