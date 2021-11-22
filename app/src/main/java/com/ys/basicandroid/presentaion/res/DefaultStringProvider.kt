package com.ys.basicandroid.presentaion.res

import android.content.Context
import com.ys.basicandroid.R
import javax.inject.Inject

class DefaultStringProvider @Inject constructor(private val context: Context): IStringResourceGetter {

    enum class Code {
        ERROR_DEFAULT,
    }

    fun getString(code: Code): String {
        return when (code) {
            Code.ERROR_DEFAULT -> getStringRes(R.string.error_default)
        }
    }

    override fun getStringRes(id: Int): String {
        return context.getString(id)
    }
}