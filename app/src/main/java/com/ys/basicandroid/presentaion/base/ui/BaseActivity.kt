package com.ys.basicandroid.presentaion.base.ui

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ys.basicandroid.domain.entity.ActionEntity
import com.ys.basicandroid.domain.entity.ClickEntity

abstract class BaseActivity<T: ViewDataBinding>(
    @LayoutRes private val layoutId: Int
) : AppCompatActivity(), NetworkHandler by NetworkHandlerImpl() {

    protected lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this

	    setBind()
	    initObserve()
	    initData()
	    setNetworkHandler(this, binding.root, this)
    }

	open fun setBind() {}

	open fun initObserve() {}

	open fun initData() {}

	open fun handleSelectEvent(entity: ClickEntity) {}

	open fun handleActionEvent(entity: ActionEntity) {}
}