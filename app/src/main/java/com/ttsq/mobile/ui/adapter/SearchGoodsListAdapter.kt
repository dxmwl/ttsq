package com.ttsq.mobile.ui.adapter

import android.content.Context
import android.graphics.Paint
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.bumptech.glide.Glide
import com.bytedance.sdk.openadsdk.TTFeedAd
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.hjq.shape.layout.ShapeLinearLayout
import com.hjq.toast.ToastUtils
import com.kwad.sdk.core.b.a.it
import com.ttsq.mobile.R
import com.ttsq.mobile.app.AppAdapter
import com.ttsq.mobile.http.api.GetPddItemsLinkApi
import com.ttsq.mobile.http.api.GetVipRatesurlApi
import com.ttsq.mobile.http.model.AdDto
import com.ttsq.mobile.http.model.DataType
import com.ttsq.mobile.http.model.GoodsDetailDto
import com.ttsq.mobile.http.model.HttpData
import com.ttsq.mobile.other.AppConfig
import com.ttsq.mobile.ui.activity.BrowserActivity
import com.ttsq.mobile.ui.activity.GoodsDetailActivity
import com.ttsq.mobile.utils.RebateUtils
import com.ttsq.mobile.utils.UIUtils


/**
 * @project : EasyBuy_Android
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/6/23
 */

/**
 * platformType : 0（淘宝） 1(京东） 2（拼多多） 3（唯品会）
 */
class SearchGoodsListAdapter(val mContext: Context, val platformType: Int = 0) :
    AppAdapter<AdDto>(mContext), LifecycleOwner {
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
            val goodsBean = getItem(position).data as GoodsDetailDto
            if (goodsImg != null) {
                val pictUrl = if (platformType == 0) {
                    goodsBean.itempic + "_310x310.jpg"
                } else {
                    goodsBean.itempic
                }
                if (pictUrl.startsWith("http") || pictUrl.startsWith("https")) {
                    Glide.with(mContext).load(pictUrl).into(goodsImg)
                } else {
                    Glide.with(mContext).load("https://${pictUrl}").into(goodsImg)
                }
            }
            goodsName?.text = if (goodsBean.itemtitle.isNullOrBlank()) {
                goodsBean.itemshorttitle
            } else {
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
                when (platformType) {
                    0 -> {
                        GoodsDetailActivity.start(mContext, goodsBean.itemid)
                    }

                    1 -> {
                        BrowserActivity.start(mContext, goodsBean.couponurl)
                    }

                    2 -> {
                        getPddItemsLink(goodsBean)
                    }

                    3 -> {
                        getVipRatesurl(goodsBean)
                    }
                }
            }
        }
    }

    private fun getVipRatesurl(goodsBean: GoodsDetailDto) {
        EasyHttp.post(this)
            .api(GetVipRatesurlApi().apply {
                goodsid = goodsBean.goodsid
                pid = AppConfig.vip_pid
            })
            .request(object : OnHttpListener<HttpData<GetVipRatesurlApi.GetVipRatesurlApiDto>> {
                override fun onSucceed(result: HttpData<GetVipRatesurlApi.GetVipRatesurlApiDto>?) {
                    result?.getData()?.url?.let { BrowserActivity.start(mContext, it) }
                }

                override fun onFail(e: Exception?) {
                    ToastUtils.show(e?.message)
                }

            })
    }

    /**
     * 拼多多转链
     */
    private fun getPddItemsLink(goodsBean: GoodsDetailDto) {
        EasyHttp.post(this)
            .api(GetPddItemsLinkApi().apply {
                goods_id = goodsBean.goods_id
                goods_sign = goodsBean.goods_sign
                pid = AppConfig.pdd_pid
            })
            .request(object : OnHttpListener<HttpData<GetPddItemsLinkApi.GetPddItemsLinkApiDto>> {
                override fun onSucceed(result: HttpData<GetPddItemsLinkApi.GetPddItemsLinkApiDto>?) {
                    result?.getData()?.mobile_url?.let { BrowserActivity.start(mContext, it) }
                }

                override fun onFail(e: Exception?) {
                    ToastUtils.show(e?.message)
                }

            })
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

    override fun getLifecycle(): Lifecycle {
        return LifecycleRegistry(this)
    }
}