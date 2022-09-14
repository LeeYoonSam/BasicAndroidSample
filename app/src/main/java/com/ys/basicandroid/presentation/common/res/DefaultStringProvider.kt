package com.ys.basicandroid.presentation.common.res

import android.content.Context
import com.ys.basicandroid.R
import com.ys.basicandroid.presentation.base.res.IStringResourceGetter
import com.ys.basicandroid.presentation.common.res.DefaultStringProvider.Code.ERROR_DEFAULT
import javax.inject.Inject

class DefaultStringProvider @Inject constructor(private val context: Context):
	IStringResourceGetter {

    enum class Code {
        ERROR_DEFAULT,
    }

    fun getString(code: Code): String {
        return when (code) {
            ERROR_DEFAULT -> getStringRes(R.string.error_default)
        }
    }

    override fun getStringRes(id: Int): String {
        return context.getString(id)
    }
}