package com.ys.basicandroid.presentaion.ui.main

import androidx.activity.viewModels
import com.ys.basicandroid.R
import com.ys.basicandroid.databinding.ActivityMainBinding
import com.ys.basicandroid.presentaion.base.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val viewModel by viewModels<MainViewModel>()

	override fun initObserve() {
		viewModel.contributors.observe(this) {
			Timber.d("contributors: $it")
		}
	}

	override fun initData() {
        viewModel.getContributors()
    }
}