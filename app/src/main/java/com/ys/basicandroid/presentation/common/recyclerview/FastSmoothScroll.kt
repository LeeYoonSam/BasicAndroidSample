package com.ys.basicandroid.presentation.common.recyclerview

import android.content.Context
import android.util.DisplayMetrics
import androidx.annotation.IntRange
import androidx.recyclerview.widget.LinearSmoothScroller

/**
 * RecyclerView의 스크롤을 빠르게 하기 위한 LinearSmoothScroller
 *
 * @param verticalSnapPreference 수직 snap
 * @param horizontalSnapPreference 수평 snap
 * @param millisecondsPerInch 스크롤 속도를 결정하는 값으로 기본값을 [MILLISECONDS_PER_INCH]로 지정. 값이 낮아지면 스크롤 속도가 빨라지고 높아지면 느려집니다.
 * @param isCenterFocusing snap 을 화면의 가운데로 지정하고 싶을때 사용.
 * 화면의 처음이나 끝 부분에 맞추고 싶다면 [verticalSnapPreference], [horizontalSnapPreference] 의 [LinearSmoothScroller.SNAP_TO_START], [LinearSmoothScroller.SNAP_TO_END] 를 사용합니다.
 * SnapPreference 보다 우선 순위가 높음
 */
class FastSmoothScroll(
    context: Context,
    @IntRange(from = SNAP_TO_START.toLong(), to = SNAP_TO_END.toLong())
    private val verticalSnapPreference: Int = SNAP_TO_START,
    @IntRange(from = SNAP_TO_START.toLong(), to = SNAP_TO_END.toLong())
    private val horizontalSnapPreference: Int = SNAP_TO_START,
    private val millisecondsPerInch: Float = MILLISECONDS_PER_INCH,
    private val isCenterFocusing: Boolean = false
) : LinearSmoothScroller(context) {

    override fun getVerticalSnapPreference(): Int {
        return verticalSnapPreference
    }

    override fun getHorizontalSnapPreference(): Int {
        return horizontalSnapPreference
    }

    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
        displayMetrics?.densityDpi?.let {
            return millisecondsPerInch / it
        }

        return super.calculateSpeedPerPixel(displayMetrics)
    }

    override fun calculateDtToFit(
        viewStart: Int,
        viewEnd: Int,
        boxStart: Int,
        boxEnd: Int,
        snapPreference: Int
    ): Int {
        return if (isCenterFocusing) {
            boxStart + (boxEnd - boxStart) / 2 - (viewStart + (viewEnd - viewStart) / 2)
        } else {
            super.calculateDtToFit(viewStart, viewEnd, boxStart, boxEnd, snapPreference)
        }
    }

    companion object {
        private const val MILLISECONDS_PER_INCH = 5f
    }
}