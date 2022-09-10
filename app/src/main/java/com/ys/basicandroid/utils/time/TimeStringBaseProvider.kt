package com.ys.basicandroid.utils.time

import com.ys.basicandroid.R
import com.ys.basicandroid.presentation.res.IStringResourceGetter

abstract class TimeStringBaseProvider : IStringResourceGetter {
    enum class Code {
        JUST_NOW,
        MIN,
        HOUR,
        DAY,
    }

    /**
     * 문구를 변경해야 하면 TimeStringBaseProvider 를 상속받아서 이 부분을 새로 구현
     */
    open fun getString(code: Code): String {
        return when (code) {
            Code.JUST_NOW -> getStringRes(R.string.format_sns_time_just_now)
            Code.MIN -> getStringRes(R.string.format_sns_time_min)
            Code.HOUR -> getStringRes(R.string.format_sns_time_hour)
            Code.DAY -> getStringRes(R.string.format_sns_time_day)
        }
    }
}