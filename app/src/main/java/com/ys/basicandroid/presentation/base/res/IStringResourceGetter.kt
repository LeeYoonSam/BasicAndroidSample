package com.ys.basicandroid.presentation.base.res

import androidx.annotation.StringRes

interface IStringResourceGetter {
    fun getStringRes(@StringRes id: Int): String
}
