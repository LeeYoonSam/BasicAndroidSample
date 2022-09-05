package com.ys.basicandroid.utils.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ys.basicandroid.utils.extensions.dp2px

class VerticalSpaceItemDecoration: RecyclerView.ItemDecoration() {
    private val decorationSize = 10.dp2px()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        with(outRect) {
            bottom = decorationSize
        }
    }
}