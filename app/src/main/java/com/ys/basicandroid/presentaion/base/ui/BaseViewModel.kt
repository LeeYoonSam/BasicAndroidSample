package com.ys.basicandroid.presentaion.base.ui

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    val isLoading = ObservableBoolean(false)

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