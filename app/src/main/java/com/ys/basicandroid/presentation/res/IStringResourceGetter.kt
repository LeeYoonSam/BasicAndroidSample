package com.ys.basicandroid.presentation.res

import androidx.annotation.StringRes

interface IStringResourceGetter {
    fun getStringRes(@StringRes id: Int): String
}
