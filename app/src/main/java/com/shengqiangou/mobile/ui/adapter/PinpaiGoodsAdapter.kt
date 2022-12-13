package com.shengqiangou.mobile.ui.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.shengqiangou.mobile.R
import com.shengqiangou.mobile.app.AppAdapter
import com.shengqiangou.mobile.http.api.RecommendPinpaiApi

class PinpaiGoodsAdapter(val mContext: Context): AppAdapter<RecommendPinpaiApi.Item>(mContext) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder:AppViewHolder(R.layout.item_pinpai_goods){

        private val goods_img:ImageView? by lazy { findViewById(R.id.goods_img) }
        private val xiaoliang:TextView? by lazy { findViewById(R.id.xiaoliang) }
        private val textView12:TextView? by lazy { findViewById(R.id.textView12) }
        private val shapeTextView2:TextView? by lazy { findViewById(R.id.shapeTextView2) }
        private val goods_price:TextView? by lazy { findViewById(R.id.goods_price) }

        override fun onBindView(position: Int) {
            val goodsDto = getItem(position)
            goods_img?.let { Glide.with(mContext).load(goodsDto.itempic).into(it) }

            xiaoliang?.text = "日销${goodsDto.todaysale}件"
            textView12?.text = "${goodsDto.itemshorttitle}"
            shapeTextView2?.text = "${goodsDto.couponmoney}元券"
            goods_price?.text = "${goodsDto.itemprice}"
        }
    }
}