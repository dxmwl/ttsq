package com.easybuy.mobile.ui.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.easybug.mobile.R
import com.easybuy.mobile.app.AppAdapter
import com.easybuy.mobile.http.api.LianxiangApi

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