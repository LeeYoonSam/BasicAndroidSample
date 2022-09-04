package com.ys.basicandroid.utils.ext

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun ViewGroup.createView(@LayoutRes layoutId: Int): View =
	LayoutInflater.from(context).inflate(layoutId, this, false)