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
class HomeGoodsListAdapter(val mContext: Context) :
    AppAdapter<HomeGoodsListApi.GoodsBean>(mContext) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.item_home_goods) {

        private val goodsImg = findViewById<ImageView>(R.id.goods_img)
        private val goodsName = findViewById<TextView>(R.id.goods_name)
        private val quanhoujia = findViewById<TextView>(R.id.quanhoujia)
        private val yuanjia = findViewById<TextView>(R.id.yuanjia)
        private val monthlySales = findViewById<TextView>(R.id.monthly_sales)
        private val yhqPrice = findViewById<TextView>(R.id.yhq_price)

        override fun onBindView(position: Int) {
            val goodsBean = getItem(position)
            if (goodsImg != null) {
                GlideApp.with(mContext).load(goodsBean.pict_url).into(goodsImg)
            }
            yuanjia?.paint?.flags = Paint.STRIKE_THRU_TEXT_FLAG
            goodsName?.text = goodsBean.title
            quanhoujia?.text = goodsBean.quanhou_jiage
            yuanjia?.text = goodsBean.size
            monthlySales?.text = "月销量 ${goodsBean.volume}"
            yhqPrice?.text = "${goodsBean.coupon_info_money}元券"

            getItemView().setOnClickListener {
                GoodsDetailActivity.start(mContext, goodsBean.tao_id, goodsBean.code)
            }
        }
    }
}