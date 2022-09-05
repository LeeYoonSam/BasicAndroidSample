package com.ys.basicandroid.presentaion.base.ui

import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.ys.basicandroid.R

abstract class BaseFragment<T : ViewDataBinding>(
    @LayoutRes private val layoutId: Int
) : Fragment() {

    protected lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBind()
        initObserve()
        initData()
        registerNetworkCallback()
    }

    private val internetConnectionSnackbar: Snackbar? by lazy {
        context?.let {
            Snackbar.make(binding.root, it.resources.getString(R.string.error_default), Snackbar.LENGTH_INDEFINITE)
        }
    }

    private fun registerNetworkCallback() {
        val connectivityManager = context?.getSystemService(ConnectivityManager::class.java)
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
}