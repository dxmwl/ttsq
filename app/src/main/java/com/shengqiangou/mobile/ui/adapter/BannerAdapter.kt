package com.shengqiangou.mobile.ui.adapter

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ConvertUtils
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.shengqiangou.mobile.http.api.HomeBannerApi
import com.shengqiangou.mobile.http.glide.GlideApp
import com.shengqiangou.mobile.other.AppConfig
import com.shengqiangou.mobile.ui.activity.GoodsListActivity
import com.shengqiangou.mobile.ui.adapter.BannerAdapter.BannerViewHolder
import com.youth.banner.adapter.BannerAdapter


/**
 * @project : YunKe
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/5/31
 */
class BannerAdapter(bannerList: ArrayList<HomeBannerApi.BannerBean>?) :
    BannerAdapter<HomeBannerApi.BannerBean, BannerViewHolder>(bannerList) {

    inner class BannerViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BannerViewHolder {
        val imageView = ImageView(parent?.context)
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP

        return BannerViewHolder(imageView)
    }

    override fun onBindView(
        holder: BannerViewHolder?,
        data: HomeBannerApi.BannerBean?,
        position: Int,
        size: Int
    ) {
        holder?.let {
            GlideApp.with(holder.itemView).load(data?.image).transform(
                MultiTransformation(
                    CenterCrop(), RoundedCorners(ConvertUtils.dp2px(8F))
                )
            ).into(holder.imageView)
        }

        holder?.imageView?.setOnClickListener {
            GoodsListActivity.start(holder.itemView.context, data?.activity_id, data?.activity_title)
        }
    }
}