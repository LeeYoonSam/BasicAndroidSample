package com.ys.basicandroid.presentation.base.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.ys.basicandroid.BR

class DataBindingViewHolder<T>(
    val binding: ViewDataBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: T) {
        binding.setVariable(BR.item, item)
    }
}