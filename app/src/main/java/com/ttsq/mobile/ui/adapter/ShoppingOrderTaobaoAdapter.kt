package com.ttsq.mobile.ui.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import com.ttsq.mobile.R
import com.ttsq.mobile.app.AppAdapter
import com.ttsq.mobile.http.api.GetShoppingTaobaoOrderListApi
import com.ttsq.mobile.ui.activity.GoodsDetailActivity

/**
 * @ClassName: ShoppingOrderTaobaoAdapter
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/04 0004 17:51
 **/
class ShoppingOrderTaobaoAdapter(val mContext: Context) :
    AppAdapter<GetShoppingTaobaoOrderListApi.TaobaoShoppingOrderDto>(mContext) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.item_order) {

        private val goods_img: RoundedImageView? by lazy { findViewById(R.id.goods_img) }
        private val textView21: TextView? by lazy { findViewById(R.id.textView21) }
        private val textView23: TextView? by lazy { findViewById(R.id.textView23) }
        private val tv_fan_money: TextView? by lazy { findViewById(R.id.tv_fan_money) }
        private val tv_time: TextView? by lazy { findViewById(R.id.tv_time) }

        override fun onBindView(position: Int) {
            val item = getItem(position)
            goods_img?.let { Glide.with(mContext).load(item.itemImg).into(it) }
            textView21?.text = item.itemTitle
            textView23?.text = item.alipayTotalPrice
            tv_fan_money?.text = item.rebatePrice
            tv_time?.text = item.createTime

            getItemView().setOnClickListener {
                GoodsDetailActivity.start(mContext, item.itemId)
            }
        }
    }
}