package com.ys.basicandroid.presentation.base.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.ys.basicandroid.domain.entity.ActionEntity
import com.ys.basicandroid.domain.entity.ClickEntity

abstract class BaseFragment<T : ViewDataBinding>(
    @LayoutRes private val layoutId: Int
) : Fragment() {

    protected lateinit var binding: T

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
    }

    open fun setBind() {}

    open fun initObserve() {}

    open fun initData() {}

	open fun handleSelectEvent(entity: ClickEntity) {}

	open fun handleActionEvent(entity: ActionEntity) {}
}