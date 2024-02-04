package com.ttsq.mobile.ui.adapter

import android.content.Context
import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hjq.shape.layout.ShapeLinearLayout
import com.hjq.shape.view.ShapeTextView
import com.ttsq.mobile.R
import com.ttsq.mobile.app.AppAdapter
import com.ttsq.mobile.http.api.CommodityScreeningApi
import com.ttsq.mobile.http.glide.GlideApp
import com.ttsq.mobile.ui.activity.BrowserActivity
import com.ttsq.mobile.ui.activity.GoodsDetailActivity
import com.ttsq.mobile.utils.RebateUtils
import java.math.BigDecimal

/**
 * @project : EasyBuy_Android
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/6/23
 */
class BangdanGoodsListAdapter2(val mContext: Context,val type:Int = 1) :
    AppAdapter<CommodityScreeningApi.ShaixuanGoodsDto>(mContext) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.item_goods_bangdan) {

        private val goodsImg = findViewById<ImageView>(R.id.goods_img)
        private val goodsName = findViewById<TextView>(R.id.goods_name)
        private val quanhoujia = findViewById<TextView>(R.id.quanhoujia)
        private val yuanjia = findViewById<TextView>(R.id.yuanjia)
        private val monthlySales = findViewById<TextView>(R.id.monthly_sales)
        private val yhqPrice = findViewById<TextView>(R.id.yhq_price)
        private val youhui_info = findViewById<ShapeTextView>(R.id.youhui_info)
        private val shop_name = findViewById<TextView>(R.id.shop_name)
        private val paiming = findViewById<TextView>(R.id.paiming)
        private val tips = findViewById<TextView>(R.id.tips)
        private val sale_num = findViewById<TextView>(R.id.sale_num)
        private val layout_fan = findViewById<ShapeLinearLayout>(R.id.layout_fan)
        private val tv_fan_money = findViewById<TextView>(R.id.tv_fan_money)

        override fun onBindView(position: Int) {
            val goodsBean = getItem(position)

            if (type==1){
                tips?.text = "近两小时已抢"
                sale_num?.text = goodsBean.itemsale2
            }else{
                tips?.text = "今日已抢"
                sale_num?.text = goodsBean.todaysale
            }
            paiming?.text = "${position + 1}"


            if (goodsImg != null) {
                GlideApp.with(mContext).load(goodsBean.itempic).into(goodsImg)
            }
            goodsName?.text = goodsBean.itemtitle

            //券后价
            quanhoujia?.text = "${goodsBean.itemendprice}"
            //返现
            tv_fan_money?.text = RebateUtils.calculateRebate(goodsBean.tkmoney)
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
}