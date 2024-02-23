package com.ttsq.mobile.ui.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.bumptech.glide.Glide
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.hjq.toast.ToastUtils
import com.makeramen.roundedimageview.RoundedImageView
import com.ttsq.mobile.R
import com.ttsq.mobile.app.AppAdapter
import com.ttsq.mobile.http.api.GetDyGoodsListApi
import com.ttsq.mobile.http.api.SwitchActivityLinkDyApi
import com.ttsq.mobile.http.model.HttpData
import com.ttsq.mobile.manager.UserManager
import com.ttsq.mobile.ui.activity.BrowserActivity
import java.lang.Exception

/**
 * @ClassName: GoodsListDyAdapter
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/22 0022 14:24
 **/
class GoodsListDyAdapter(val mContext: Context) :
    AppAdapter<GetDyGoodsListApi.DyGoodsDto>(mContext),
    LifecycleOwner {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.item_dy_goods) {

        private val tv_shop_name: TextView? by lazy { findViewById(R.id.tv_shop_name) }
        private val tv_distance: TextView? by lazy { findViewById(R.id.tv_distance) }
        private val goods_img: RoundedImageView? by lazy { findViewById(R.id.goods_img) }
        private val goods_title: TextView? by lazy { findViewById(R.id.goods_title) }
        private val currient_price: TextView? by lazy { findViewById(R.id.currient_price) }
        private val tv_original_price: TextView? by lazy { findViewById(R.id.tv_original_price) }
        private val tv_sale_num: TextView? by lazy { findViewById(R.id.tv_sale_num) }
        private val btn_buy: TextView? by lazy { findViewById(R.id.btn_buy) }
        private val linearLayout5: LinearLayout? by lazy { findViewById(R.id.linearLayout5) }
        override fun onBindView(position: Int) {
            val item = getItem(position)
            linearLayout5?.removeAllViews()
            item.item_tag?.forEach {
                val view =
                    LayoutInflater.from(mContext).inflate(R.layout.layout_goods_label, null)
                val tvLabel = view.findViewById<TextView>(R.id.tv_goods_label)
                tvLabel?.text = it.content
                linearLayout5?.addView(view)
            }
            goods_img?.let { Glide.with(mContext).load(item.item_img).into(it) }
            tv_shop_name?.text = item.shop_name
            tv_distance?.text = item.distance
            goods_title?.text = item.item_title
            currient_price?.text = "¥${item.price}"
            tv_original_price?.text = "¥${item.origin_price}"
            tv_sale_num?.text = "月售${item.sold}"
            tv_original_price?.paint?.flags = Paint.STRIKE_THRU_TEXT_FLAG

            btn_buy?.setOnClickListener {
                switchActivityLinkDy(item)
            }
        }

    }

    private fun switchActivityLinkDy(item: GetDyGoodsListApi.DyGoodsDto) {
        EasyHttp.post(this)
            .api(SwitchActivityLinkDyApi().apply {
                id = item.id
                channel = UserManager.userInfo?.userId
            })
            .request(object : OnHttpListener<HttpData<SwitchActivityLinkDyApi.DyLinkDto>> {
                override fun onSucceed(result: HttpData<SwitchActivityLinkDyApi.DyLinkDto>?) {
                    result?.getData()?.let {
                        BrowserActivity.start(mContext, it.share_link)
                    }
                }

                override fun onFail(e: Exception?) {
                    ToastUtils.show(e?.message)
                }

            })
    }

    override fun getLifecycle(): Lifecycle {
        return LifecycleRegistry(this)
    }
}