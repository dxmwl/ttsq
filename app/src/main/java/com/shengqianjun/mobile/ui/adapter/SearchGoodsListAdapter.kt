package com.shengqianjun.mobile.ui.adapter

import android.content.Context
import android.graphics.Paint
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.shengqianjun.mobile.R
import com.shengqianjun.mobile.app.AppAdapter
import com.shengqianjun.mobile.http.glide.GlideApp
import com.shengqianjun.mobile.http.model.GoodsDetailDto
import com.shengqianjun.mobile.ui.activity.GoodsDetailActivity

/**
 * @project : EasyBuy_Android
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/6/23
 */
class SearchGoodsListAdapter(val mContext: Context) :
    AppAdapter<GoodsDetailDto>(mContext) {
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