package com.ttsq.mobile.ui.adapter

import android.content.Context
import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bytedance.sdk.openadsdk.TTFeedAd
import com.hjq.shape.layout.ShapeLinearLayout
import com.hjq.shape.view.ShapeTextView
import com.ttsq.mobile.R
import com.ttsq.mobile.app.AppAdapter
import com.ttsq.mobile.http.api.CommodityScreeningApi
import com.ttsq.mobile.http.glide.GlideApp
import com.ttsq.mobile.http.model.AdDto
import com.ttsq.mobile.http.model.DataType
import com.ttsq.mobile.http.model.GoodsDetailDto
import com.ttsq.mobile.ui.activity.GoodsDetailActivity
import com.ttsq.mobile.utils.UIUtils

/**
 * @project : EasyBuy_Android
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/6/23
 */
class ShaixuanGoodsListAdapter(val mContext: Context) :
    AppAdapter<AdDto>(mContext) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        if (viewType == 0) {
            return ViewHolder()
        } else {
            return AdViewHolder()
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        if (item.type == DataType.DATE) {
            return 0
        } else {
            return 1
        }
    }

    inner class ViewHolder : AppViewHolder(R.layout.item_goods_vertical) {

        private val goodsImg = findViewById<ImageView>(R.id.goods_img)
        private val goodsName = findViewById<TextView>(R.id.goods_name)
        private val quanhoujia = findViewById<TextView>(R.id.quanhoujia)
        private val yuanjia = findViewById<TextView>(R.id.yuanjia)
        private val monthlySales = findViewById<TextView>(R.id.monthly_sales)
        private val yhqPrice = findViewById<TextView>(R.id.yhq_price)
        private val youhui_info = findViewById<ShapeTextView>(R.id.youhui_info)
        private val shop_name = findViewById<TextView>(R.id.shop_name)
        private val layout_fan = findViewById<ShapeLinearLayout>(R.id.layout_fan)
        private val tv_fan_money = findViewById<TextView>(R.id.tv_fan_money)

        override fun onBindView(position: Int) {
            val goodsBean = getItem(position).data as CommodityScreeningApi.ShaixuanGoodsDto
            if (goodsImg != null) {
                GlideApp.with(mContext).load(goodsBean.itempic).into(goodsImg)
            }
            goodsName?.text = goodsBean.itemtitle

            //券后价
            quanhoujia?.text = "${goodsBean.itemendprice}"
            //返现
            tv_fan_money?.text = "${goodsBean.tkmoney}"
            //原价
            yuanjia?.text = "￥${goodsBean.itemprice}"
            yuanjia?.paint?.flags = Paint.STRIKE_THRU_TEXT_FLAG

            monthlySales?.text = "月销量 ${goodsBean.itemsale}"

            yhqPrice?.text = "${goodsBean.couponmoney}元券"

            if (goodsBean.couponinfo.isBlank()) {
                youhui_info?.visibility = View.GONE
            } else {
                youhui_info?.visibility = View.VISIBLE
                youhui_info?.text = goodsBean.couponinfo
            }
            shop_name?.text = goodsBean.shopname
            getItemView().setOnClickListener {
                GoodsDetailActivity.start(mContext, goodsBean.itemid)
            }
        }
    }

    inner class AdViewHolder : AppViewHolder(R.layout.item_goods_ad) {
        private val listitem_ad_express: FrameLayout? by lazy { findViewById(R.id.listitem_ad_express) }
        override fun onBindView(position: Int) {
            val item = getItem(position).data as TTFeedAd
            UIUtils.removeFromParent(item.adView);
            listitem_ad_express?.removeAllViews();
            listitem_ad_express?.addView(item.adView)
        }
    }
}