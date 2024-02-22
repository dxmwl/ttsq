package com.ttsq.mobile.ui.adapter

import android.content.Context
import android.graphics.Paint
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hjq.shape.layout.ShapeLinearLayout
import com.ttsq.mobile.R
import com.ttsq.mobile.app.AppAdapter
import com.ttsq.mobile.http.api.PinpaiDetailApi
import com.ttsq.mobile.http.glide.GlideApp
import com.ttsq.mobile.ui.activity.GoodsDetailActivity
import com.ttsq.mobile.utils.RebateUtils

/**
 * @project : EasyBuy_Android
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/6/23
 */
class PinpaiDetailGoodsListAdapter(val mContext: Context) :
    AppAdapter<PinpaiDetailApi.Item>(mContext) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.item_goods) {

        private val goodsImg = findViewById<ImageView>(R.id.goods_img)
        private val goodsName = findViewById<TextView>(R.id.goods_name)
        private val quanhoujia = findViewById<TextView>(R.id.quanhoujia)
        private val yuanjia = findViewById<TextView>(R.id.yuanjia)
        private val monthlySales = findViewById<TextView>(R.id.monthly_sales)
        private val yhqPrice = findViewById<TextView>(R.id.yhq_price)
        private val layout_fan = findViewById<ShapeLinearLayout>(R.id.layout_fan)
        private val tv_fan_money = findViewById<TextView>(R.id.tv_fan_money)

        override fun onBindView(position: Int) {
            val goodsBean = getItem(position)
            if (goodsImg != null) {
                val pictUrl = goodsBean.itempic
                if (pictUrl.startsWith("http") || pictUrl.startsWith("https")) {
                    GlideApp.with(mContext).load(pictUrl).into(goodsImg)
                } else {
                    GlideApp.with(mContext).load("https://${pictUrl}").into(goodsImg)
                }
            }
            goodsName?.text = if (goodsBean.itemtitle.isNullOrBlank()){
                goodsBean.itemshorttitle
            }else{
                goodsBean.itemtitle
            }

            //券后价
            quanhoujia?.text = "${goodsBean.itemendprice}"
            //返现
            tv_fan_money?.text = RebateUtils.calculateRebate(goodsBean.tkmoney)
            //原价
            yuanjia?.text = "￥${goodsBean.itemprice}"
            yuanjia?.paint?.flags = Paint.STRIKE_THRU_TEXT_FLAG
            monthlySales?.text = "月销量 ${goodsBean.itemsale}"
            yhqPrice?.text = "${goodsBean.couponmoney}元券"
            getItemView().setOnClickListener {
                GoodsDetailActivity.start(mContext, goodsBean.itemid)
            }
        }
    }
}