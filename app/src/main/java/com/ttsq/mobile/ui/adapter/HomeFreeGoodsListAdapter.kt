package com.ttsq.mobile.ui.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import com.ttsq.mobile.R
import com.ttsq.mobile.app.AppAdapter
import com.ttsq.mobile.http.api.GetFreeGoodsListApi

/**
 * @ClassName: FreeGoodsListAdapter
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/05 0005 11:41
 **/
class HomeFreeGoodsListAdapter(val mContext: Context) :
    AppAdapter<GetFreeGoodsListApi.FreeGoodsDeo>(mContext) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.item_free_goods) {

        private val roundedImageView: RoundedImageView? by lazy { findViewById(R.id.roundedImageView) }
        override fun onBindView(position: Int) {
            val item = getItem(position)
            roundedImageView?.let { Glide.with(mContext).load(item.goodsImg).into(it) }
        }
    }
}