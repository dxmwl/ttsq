package com.shengqiangou.mobile.ui.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.shengqiangou.mobile.R
import com.shengqiangou.mobile.app.AppAdapter
import com.shengqiangou.mobile.http.glide.GlideApp
import com.shengqiangou.mobile.http.model.MenuDto

/**
 * @project : YunKe
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/5/31
 */
class HomeMenuListAdapter(val mContext: Context) : AppAdapter<MenuDto>(mContext) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.item_home_menu) {

        private val imageView4: ImageView? by lazy { findViewById(R.id.imageView4) }
        private val title: TextView? by lazy { findViewById(R.id.title) }

        override fun onBindView(position: Int) {
            val item = getItem(position)
            imageView4?.let { GlideApp.with(mContext).load(item.resId).into(it) }
            title?.text = item.title
        }

    }
}