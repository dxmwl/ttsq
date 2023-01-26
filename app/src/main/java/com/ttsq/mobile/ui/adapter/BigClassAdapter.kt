package com.ttsq.mobile.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ttsq.mobile.R
import com.ttsq.mobile.app.AppAdapter
import com.hjq.shape.view.ShapeTextView
import com.ttsq.mobile.http.api.ClassApi

class BigClassAdapter(mContext: Context) : AppAdapter<ClassApi.ClassInfo>(mContext) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return IndustryListViewHolder()
    }

    inner class IndustryListViewHolder : AppViewHolder(R.layout.item_industry) {

        private val title: TextView? by lazy { findViewById(R.id.title) }
        private val indicator: ShapeTextView? by lazy { findViewById(R.id.indicator) }

        override fun onBindView(position: Int) {
            val item = getItem(position)
            title?.text = item.main_name
            if (item.checked) {
                title?.setTextColor(Color.parseColor("#FF333333"))
                title?.textSize = 16F
                indicator?.visibility = View.VISIBLE
            } else {
                title?.setTextColor(Color.parseColor("#FFB3B3B3"))
                title?.textSize = 14F
                indicator?.visibility = View.INVISIBLE
            }
        }

    }
}