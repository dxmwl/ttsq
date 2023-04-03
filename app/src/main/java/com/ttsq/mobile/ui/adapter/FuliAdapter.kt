package com.ttsq.mobile.ui.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ttsq.mobile.R
import com.ttsq.mobile.app.AppAdapter
import com.ttsq.mobile.ui.activity.BrowserActivity
import com.ttsq.mobile.ui.fragment.FuliFragment

/**
 * @ClassName: FuliAdapter
 * @Description:
 * @Author: 常利兵
 * @Date: 2023/4/3 11:52
 **/
class FuliAdapter(val mContext: Context) : AppAdapter<FuliFragment.FuliItemDto>(mContext) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.item_fuli) {

        private val imageView6: ImageView? by lazy { findViewById(R.id.imageView6) }
        private val tv_title: TextView? by lazy { findViewById(R.id.tv_title) }

        override fun onBindView(position: Int) {
            val fuliItemDto = getItem(position)

            imageView6?.let { Glide.with(mContext).load(fuliItemDto.bannerImg).into(it) }
            tv_title?.text = fuliItemDto.title

            getItemView().setOnClickListener {
                BrowserActivity.start(mContext, fuliItemDto.url)
            }
        }
    }
}