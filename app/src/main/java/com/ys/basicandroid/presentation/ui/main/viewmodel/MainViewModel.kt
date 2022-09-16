package com.ys.basicandroid.presentation.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ys.basicandroid.domain.Result
import com.ys.basicandroid.domain.getOrDefault
import com.ys.basicandroid.domain.github.GetContributorsUseCase
import com.ys.basicandroid.presentation.base.viewmodel.BaseViewModel
import com.ys.basicandroid.common.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getContributorsUseCase: GetContributorsUseCase
): BaseViewModel() {

    private val _contributors = MutableLiveData<List<User>>(emptyList())
    val contributors: LiveData<List<User>> = _contributors

    fun getContributors() {
        viewModelScope.launch {
            val result = getContributorsUseCase(
                GetContributorsUseCase.Param(
                    owner = "LeeYoonSam",
                    name = "BasicAndroidSample",
                    pageNo = 1
                )
            )

            when (result) {
                is Result.Success -> {
                    _contributors.value = result.getOrDefault(emptyList())
                    hideLoading()
                }

                is Result.Error -> {
                    _contributors.value = emptyList()
                    hideLoading()
                }

                is Result.Loading -> {
                    showLoading()
                }
            }
        }
    }
}