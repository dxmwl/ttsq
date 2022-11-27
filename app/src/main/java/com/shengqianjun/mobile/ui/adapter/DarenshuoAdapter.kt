package com.shengqianjun.mobile.ui.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.shengqianjun.mobile.R
import com.shengqianjun.mobile.app.AppAdapter
import com.shengqianjun.mobile.http.api.DarenshuoApi

/**
 * 朋友圈
 */
class DarenshuoAdapter(val mContext: Context) : AppAdapter<DarenshuoApi.Newdata>(mContext) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.item_darenshuo) {

        private val thumb: ImageView? by lazy { findViewById(R.id.thumb) }
        private val title: TextView? by lazy { findViewById(R.id.title) }
        private val desc: TextView? by lazy { findViewById(R.id.desc) }

        override fun onBindView(position: Int) {
            val pyqGoodsDto = getItem(position)

            thumb?.let { Glide.with(mContext).load(pyqGoodsDto.article_banner).into(it) }
            title?.text = pyqGoodsDto.name
            desc?.text = pyqGoodsDto.article
        }
    }
}