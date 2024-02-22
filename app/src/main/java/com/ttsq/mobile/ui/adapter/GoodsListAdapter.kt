package com.ttsq.mobile.ui.adapter

import android.content.Context
import android.graphics.Paint
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.ConvertUtils
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.ttsq.mobile.R
import com.ttsq.mobile.app.AppAdapter
import com.ttsq.mobile.http.api.CommodityScreeningApi
import com.ttsq.mobile.http.glide.GlideApp
import com.ttsq.mobile.ui.activity.GoodsDetailActivity
import com.ttsq.mobile.utils.FormatUtils
import com.ttsq.mobile.utils.RebateUtils

/**
 * @project : EasyBuy_Android
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/6/23
 */
class GoodsListAdapter(val mContext: Context) :
    AppAdapter<CommodityScreeningApi.ShaixuanGoodsDto>(mContext) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.item_mrbk_goods) {

        private val goodsImg = findViewById<ImageView>(R.id.goods_pic)
        private val goodsName = findViewById<TextView>(R.id.goods_name)
        private val quanhoujia = findViewById<TextView>(R.id.goods_price)
        private val yuanjia = findViewById<TextView>(R.id.yuanjia)
        private val monthlySales = findViewById<TextView>(R.id.monthly_sales)
        private val yhqPrice = findViewById<TextView>(R.id.yhq_price)
        private val shop_name = findViewById<TextView>(R.id.shop_name)
        private val tv_fan_money = findViewById<TextView>(R.id.tv_fan_money)
        override fun onBindView(position: Int) {
            val goodsBean = getItem(position)
            if (goodsImg != null) {
                GlideApp.with(mContext).load(goodsBean.itempic).transform(
                    MultiTransformation(
                        CenterCrop(), RoundedCorners(
                            ConvertUtils.dp2px(6F)
                        )
                    )
                ).into(goodsImg)
            }
            yuanjia?.paint?.flags = Paint.STRIKE_THRU_TEXT_FLAG
            goodsName?.text = goodsBean.itemtitle
            quanhoujia?.text = goodsBean.itemendprice
            shop_name?.text = goodsBean.shopname
            yuanjia?.text = goodsBean.itemprice
            //返现
            tv_fan_money?.text = RebateUtils.calculateRebate(goodsBean.tkmoney)
            FormatUtils.formatSales(monthlySales, goodsBean.itemsale)
            yhqPrice?.text = "${goodsBean.couponmoney}元券"

            getItemView().setOnClickListener {
                GoodsDetailActivity.start(mContext, goodsBean.itemid)
            }
        }
    }
}