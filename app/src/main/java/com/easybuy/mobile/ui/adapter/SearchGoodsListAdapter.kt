package com.easybuy.mobile.ui.adapter

import android.content.Context
import android.graphics.Paint
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.easybug.mobile.R
import com.easybuy.mobile.app.AppAdapter
import com.easybuy.mobile.http.api.HomeGoodsListApi
import com.easybuy.mobile.http.glide.GlideApp
import com.easybuy.mobile.ui.activity.GoodsDetailActivity

/**
 * @project : EasyBuy_Android
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/6/23
 */
class SearchGoodsListAdapter(val mContext: Context) :
    AppAdapter<HomeGoodsListApi.GoodsBean>(mContext) {
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
                val pictUrl = goodsBean.pict_url
                if (pictUrl.startsWith("http") || pictUrl.startsWith("https")) {
                    GlideApp.with(mContext).load(pictUrl).into(goodsImg)
                } else {
                    GlideApp.with(mContext).load("https://${pictUrl}").into(goodsImg)
                }
            }
            goodsName?.text = goodsBean.title

            //券后价
            val quanhouJiage = goodsBean.quanhou_jiage
            if (quanhouJiage.isNullOrBlank()) {
                quanhoujia?.text = "${goodsBean.zk_final_price.subtract(goodsBean.coupon_amount)}"
            } else {
                quanhoujia?.text = "${quanhouJiage}"
            }
            //原价
            val size = goodsBean.size
            if (size.isNullOrBlank()) {
                yuanjia?.text = "￥${goodsBean.zk_final_price}"
            } else {
                yuanjia?.text = "￥${size}"

            }
            yuanjia?.paint?.flags = Paint.STRIKE_THRU_TEXT_FLAG
            monthlySales?.text = "月销量 ${goodsBean.volume}"
            val couponInfoMoney = goodsBean.coupon_info_money
            if (couponInfoMoney.isNullOrBlank()) {
                yhqPrice?.text = "${goodsBean.coupon_amount}元券"
            } else {
                yhqPrice?.text = "${couponInfoMoney}元券"
            }
            getItemView().setOnClickListener {
                val taoId = goodsBean.tao_id
                if (taoId.isNullOrBlank()) {
                    GoodsDetailActivity.start(mContext, goodsBean.item_id, goodsBean.code)
                } else {
                    GoodsDetailActivity.start(mContext, taoId, goodsBean.code)
                }
            }
        }
    }
}