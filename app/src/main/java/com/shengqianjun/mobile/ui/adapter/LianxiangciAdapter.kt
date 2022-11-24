package com.shengqianjun.mobile.ui.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.shengqianjun.mobile.R
import com.shengqianjun.mobile.app.AppAdapter
import com.shengqianjun.mobile.http.api.LianxiangApi

class LianxiangciAdapter(mContext: Context): AppAdapter<LianxiangApi.LianxiangDto>(mContext) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder:AppViewHolder(R.layout.item_keyword_match){

        private val tv_keyword:TextView?by lazy { findViewById(R.id.tv_keyword) }

        override fun onBindView(position: Int) {
            val lianxiangDto = getItem(position)
            tv_keyword?.text = lianxiangDto[0]
        }
    }
}