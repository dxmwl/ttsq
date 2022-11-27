package com.shengqianjun.mobile.ui.adapter

import android.content.Context
import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hjq.shape.view.ShapeTextView
import com.shengqianjun.mobile.R
import com.shengqianjun.mobile.app.AppAdapter
import com.shengqianjun.mobile.http.api.ArticleDetailApi
import com.shengqianjun.mobile.http.api.CommodityScreeningApi
import com.shengqianjun.mobile.http.glide.GlideApp
import com.shengqianjun.mobile.ui.activity.GoodsDetailActivity

/**
 * @project : EasyBuy_Android
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/6/23
 */
class ArticelGoodsListAdapter(val mContext: Context) :
    AppAdapter<ArticleDetailApi.Item>(mContext) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
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

        override fun onBindView(position: Int) {
            val goodsBean = getItem(position)
            if (goodsImg != null) {
                GlideApp.with(mContext).load(goodsBean.itempic).into(goodsImg)
            }
            goodsName?.text = goodsBean.itemtitle

            //券后价
            quanhoujia?.text = "${goodsBean.itemendprice}"
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