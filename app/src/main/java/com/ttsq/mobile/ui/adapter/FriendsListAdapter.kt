package com.ttsq.mobile.ui.adapter

import android.content.Context
import android.view.ViewGroup
import com.ttsq.mobile.R
import com.ttsq.mobile.app.AppAdapter

/**
 * @ClassName: FriendsListAdapter
 * @Description:
 * @Author: 常利兵
 * @Date: 2023/6/24 23:28
 **/
class FriendsListAdapter(mContext:Context) :AppAdapter<String>(mContext){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder:AppViewHolder(R.layout.item_invite_friend){
        override fun onBindView(position: Int) {

        }
    }
}