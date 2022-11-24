package com.shengqianjun.mobile.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @project : YunKe
 * @Description : SpacesItemDecoration
 * @author : clb
 * @time : 2022/6/6
 */
class SpacesItemDecoration(val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager is GridLayoutManager) {
            outRect.top = space
            outRect.right = space
        }
    }
}
