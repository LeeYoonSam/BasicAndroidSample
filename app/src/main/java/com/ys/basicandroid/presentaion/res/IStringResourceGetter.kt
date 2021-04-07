package com.ys.basicandroid.presentaion.res

import androidx.annotation.StringRes

interface IStringResourceGetter {
    fun getStringRes(@StringRes id: Int): String
}
