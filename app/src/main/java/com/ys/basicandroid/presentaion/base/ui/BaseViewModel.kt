package com.ys.basicandroid.presentaion.base.ui

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    val isLoading = ObservableBoolean(false)

    val _error: MutableLiveData<String> = MutableLiveData()
    val error: LiveData<String> = _error

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

    fun dispatchError(message: String) {
        _error.value = message
    }
}