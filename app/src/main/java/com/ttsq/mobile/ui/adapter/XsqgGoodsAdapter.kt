package com.ttsq.mobile.ui.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.hjq.shape.layout.ShapeLinearLayout
import com.hjq.shape.view.ShapeTextView
import com.hjq.toast.ToastUtils
import com.ttsq.mobile.R
import com.ttsq.mobile.app.AppAdapter
import com.ttsq.mobile.http.glide.GlideApp
import com.ttsq.mobile.http.model.GoodsDetailDto
import com.ttsq.mobile.ui.activity.GoodsDetailActivity
import com.ttsq.mobile.utils.RebateUtils
import java.math.BigDecimal

/**
 * @project : EasyBuy_Android
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/6/23
 */
class XsqgGoodsAdapter(val mContext: Context, val type: Int = 1) :
    AppAdapter<GoodsDetailDto>(mContext) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.item_xsqg_goods) {

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
        private val go_buy = findViewById<ShapeTextView>(R.id.go_buy)
        private val progress = findViewById<ProgressBar>(R.id.progress)
        private val layout_fan = findViewById<ShapeLinearLayout>(R.id.layout_fan)
        private val tv_fan_money = findViewById<TextView>(R.id.tv_fan_money)

        override fun onBindView(position: Int) {
            val goodsBean = getItem(position)

            sale_num?.text = "${goodsBean.virtual_sum}"
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

//            if (goodsBean.couponinfo.isBlank()) {
//                youhui_info?.visibility = View.GONE
//            } else {
//                youhui_info?.visibility = View.VISIBLE
//                youhui_info?.text = goodsBean.couponinfo
//            }

            when (goodsBean.grab_type) {
                1 -> {
                    progress?.progress = 0
                    go_buy?.text = "即将开抢"
                    go_buy?.shapeDrawableBuilder
                        ?.setSolidColor(Color.parseColor("#B5B5B5"))
                        // 注意：最后需要调用一下 intoBackground 方法才能生效
                        ?.intoBackground();
                }
                2 -> {
                    progress?.progress = 100
                    go_buy?.text = "已抢光"
                    go_buy?.shapeDrawableBuilder
                        ?.setSolidColor(Color.parseColor("#B5B5B5"))
                        // 注意：最后需要调用一下 intoBackground 方法才能生效
                        ?.intoBackground();
                }
                3 -> {
                    var i = goodsBean.virtual_sum % 100
                    if (i == 0) {
                        i = 50
                    }
                    progress?.progress = i
                    go_buy?.text = "马上抢"
                    go_buy?.shapeDrawableBuilder
                        ?.setSolidColor(Color.parseColor("#FF0000"))
                        // 注意：最后需要调用一下 intoBackground 方法才能生效
                        ?.intoBackground();
                }
            }

            shop_name?.text = goodsBean.shopname
            getItemView().setOnClickListener {
                if (goodsBean.grab_type == 3) {
                    GoodsDetailActivity.start(mContext, goodsBean.itemid)
                } else if (goodsBean.grab_type == 2) {
                    ToastUtils.show("快抢已结束")
                } else {
                    ToastUtils.show("快抢即将开始")
                }
            }
        }
    }
}