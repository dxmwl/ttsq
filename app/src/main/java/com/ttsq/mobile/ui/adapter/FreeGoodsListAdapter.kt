package com.ttsq.mobile.ui.adapter

import android.content.Context
import android.graphics.Paint
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
class FreeGoodsListAdapter(val mContext: Context) :
    AppAdapter<GetFreeGoodsListApi.FreeGoodsDeo>(mContext) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.item_free_goods_list) {

        private val roundedImageView: RoundedImageView? by lazy { findViewById(R.id.roundedImageView) }
        private val goods_name: TextView? by lazy { findViewById(R.id.goods_name) }
        private val yhq_price: TextView? by lazy { findViewById(R.id.yhq_price) }
        private val tv_fan_money: TextView? by lazy { findViewById(R.id.tv_fan_money) }
        private val yuanjia: TextView? by lazy { findViewById(R.id.yuanjia) }
        private val btn_buy: TextView? by lazy { findViewById(R.id.btn_buy) }
        override fun onBindView(position: Int) {
            val item = getItem(position)
            roundedImageView?.let { Glide.with(mContext).load(item.goodsImg).into(it) }
            goods_name?.text = item.goodsTitle
            yhq_price?.text = item.couponsMoney
            tv_fan_money?.text = item.tkRate
            yuanjia?.text = item.goodsZkPrice
            yuanjia?.paint?.flags = Paint.STRIKE_THRU_TEXT_FLAG

            btn_buy?.setOnClickListener {
                listener?.buyFreeGoods(item.goodsId, item.goodsTitle)
            }
        }
    }

    private var listener: OnFreeGoodsClick? = null

    interface OnFreeGoodsClick {
        fun buyFreeGoods(goodsId: String, goodsName: String)

    }

    fun setClickListener(listener: OnFreeGoodsClick) {
        this.listener = listener
    }
}