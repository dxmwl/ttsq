package com.easybuy.mobile.ui.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.easybug.mobile.R
import com.easybuy.mobile.app.AppAdapter
import com.easybuy.mobile.http.api.YqbkApi
import com.easybuy.mobile.ui.activity.GoodsDetailActivity
import com.hjq.toast.ToastUtils


class YqbkListAdapter(val mContext: Context) : AppAdapter<YqbkApi.YqbkGoodsInfo>(mContext) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.item_yqbk),
        BGANinePhotoLayout.Delegate {
        private val shop_icon: ImageView? by lazy { findViewById(R.id.shop_icon) }
        private val shop_name: TextView? by lazy { findViewById(R.id.shop_name) }
        private val send_time: TextView? by lazy { findViewById(R.id.send_time) }
        private val ljlq_btn: TextView? by lazy { findViewById(R.id.ljlq_btn) }
        private val wa: TextView? by lazy { findViewById(R.id.wa) }
        private val nine_img_view: BGANinePhotoLayout? by lazy { findViewById(R.id.nine_img_view) }

        private val quanhoujia: TextView? by lazy { findViewById(R.id.quanhoujia) }
        private val save_img: LinearLayout? by lazy { findViewById(R.id.save_img) }
        private val copy_kl: LinearLayout? by lazy { findViewById(R.id.copy_kl) }
        private val tv_copy_wa: TextView? by lazy { findViewById(R.id.tv_copy_wa) }

        override fun onBindView(position: Int) {
            val goodsInfo = getItem(position)

            shop_icon?.let {
                Glide.with(mContext).load(goodsInfo.shopIcon)
                    .apply(
                        RequestOptions.bitmapTransform(CircleCrop())
                    )
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(it)
            }
            shop_name?.text = goodsInfo.shop_title
            send_time?.text = goodsInfo.date_time
            quanhoujia?.text = "券后价￥${goodsInfo.quanhou_jiage}"

            val nineImgList = goodsInfo.small_images
            if (nineImgList.isEmpty().not()) {
                val split = nineImgList.split("|")
                val imgList = ArrayList<String>()
                split.forEach {
                    imgList.add(it)
                }
                nine_img_view?.setDelegate(this)
                nine_img_view?.data = imgList
            }
            //文案
            val waStr = if (goodsInfo.tag.isEmpty()) {
                goodsInfo.jianjie
            } else {
                goodsInfo.tag
            }
            wa?.text = waStr
            tv_copy_wa?.setOnClickListener {
                val cm =
                    it.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                // 创建普通字符型ClipData
                val mClipData = ClipData.newPlainText("Label", waStr)
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData)
                ToastUtils.show("文案复制成功")
            }

            //复制口令
            copy_kl?.setOnClickListener {
//                    val hashMap = HashMap<String, Any?>()
//                    hashMap["itemid"] = item.tao_id
//                    hashMap["title"] = item.tao_title
//                    AppUtils.getGoodsKl(hashMap)
            }

            //保存图片
            save_img?.setOnClickListener {
                ToastUtils.show("开始下载图片...")
//                    imgList.forEachIndexed { index, s ->
//                        DonwloadSaveImg.donwloadImg(it.context, s, object : ImgDownloadListener {
//                            override fun imgDownloadFail() {
//                                ToastUtils.showShort("第${index + 1}张图片下载失败!")
//                            }
//
//                            override fun imgDownloadSuccess(filePath: String) {
//                                ToastUtils.showShort("第${index + 1}张图片下载成功!")
//                                val data: Uri = Uri.parse("file://$filePath")
//                                it.context.sendBroadcast(
//                                    Intent(
//                                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
//                                        data
//                                    )
//                                )
//                            }
//                        })
//                    }
            }

            getItemView().setOnClickListener {
                GoodsDetailActivity.start(mContext, goodsInfo.tao_id, goodsInfo.code)
            }
        }

        override fun onClickNinePhotoItem(
            ninePhotoLayout: BGANinePhotoLayout?,
            view: View?,
            position: Int,
            model: String?,
            models: MutableList<String>?
        ) {
            if (ninePhotoLayout != null) {
                val mContext = ninePhotoLayout.context
                val previewActivity = BGAPhotoPreviewActivity.IntentBuilder(mContext)
                if (ninePhotoLayout.itemCount == 1) {
                    // 预览单张图片
                    previewActivity.previewPhoto(ninePhotoLayout.currentClickItem)
                } else if (ninePhotoLayout.itemCount > 1) {
                    // 预览多张图片
                    previewActivity.previewPhotos(ninePhotoLayout.data)
                        .currentPosition(ninePhotoLayout.currentClickItemPosition) // 当前预览图片的索引
                }
                mContext.startActivity(previewActivity.build())
            }
        }

        override fun onClickExpand(
            ninePhotoLayout: BGANinePhotoLayout?,
            view: View?,
            position: Int,
            model: String?,
            models: MutableList<String>?
        ) {
            ninePhotoLayout?.setIsExpand(true)
            ninePhotoLayout?.flushItems()
        }
    }
}