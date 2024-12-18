package com.ttsq.mobile.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ttsq.mobile.R
import com.ttsq.mobile.app.AppAdapter
import com.ttsq.mobile.http.api.ZhutiPinpaiApi
import com.ttsq.mobile.ui.activity.GoodsDetailActivity

class ZhutiPinpaiListAdapter(val mContext: Context) :
    AppAdapter<ZhutiPinpaiApi.ZhutiPinpaiDto>(mContext) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.item_goods_pinpai) {
        private val pinpai_banner: ImageView? by lazy { findViewById(R.id.pinpai_banner) }
        private val logo_img: ImageView? by lazy { findViewById(R.id.logo_img) }
        private val textView11: TextView? by lazy { findViewById(R.id.textView11) }
        private val pinpai_biaoyu: TextView? by lazy { findViewById(R.id.pinpai_biaoyu) }
        private val pinpai_goods_list: RecyclerView? by lazy { findViewById(R.id.pinpai_goods_list) }

        override fun onBindView(position: Int) {
            val zhutiPinpaiDto = getItem(position)

            logo_img?.let { it1 ->
                Glide.with(mContext).load(zhutiPinpaiDto.brand_logo).into(it1)
            }
            textView11?.text = zhutiPinpaiDto.brand_name
//            pinpai_biaoyu?.text = zhutiPinpaiDto.title

            if (zhutiPinpaiDto.items.isNullOrEmpty().not()) {
                pinpai_goods_list?.let {
                    it.layoutManager =
                        LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
                   val pinpaiGoodsAdapter = PinpaiGoodsAdapter(mContext)
                    pinpaiGoodsAdapter.setOnItemClickListener(object :
                        OnItemClickListener {
                        override fun onItemClick(
                            recyclerView: RecyclerView?,
                            itemView: View?,
                            position: Int,
                        ) {
                            val item = pinpaiGoodsAdapter.getItem(position)
                            GoodsDetailActivity.start(mContext, item.itemid)
                        }

                    })
                    it.adapter = pinpaiGoodsAdapter
                    pinpaiGoodsAdapter.setData(zhutiPinpaiDto.items)
                }
            }
        }
    }
}