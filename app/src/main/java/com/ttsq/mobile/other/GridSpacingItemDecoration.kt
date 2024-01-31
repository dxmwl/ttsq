package com.ttsq.mobile.other

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @ClassName: GridSpacingItemDecoration
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/1/31 0031 22:14
 **/
class GridSpacingItemDecoration(val spanCount: Int, val spacing: Int, val includeEdge: Boolean) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount
        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount
            outRect.right = (column + 1) * spacing / spanCount
        } else {
            outRect.left = column * spacing / spanCount
            outRect.right = spacing - (column + 1) * spacing / spanCount
        }
        if (position < spanCount) {
            outRect.top = spacing
        }
        outRect.bottom = spacing
    }
}