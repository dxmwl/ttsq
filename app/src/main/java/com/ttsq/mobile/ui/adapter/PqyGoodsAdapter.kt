package com.ttsq.mobile.ui.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout
import com.blankj.utilcode.util.TimeUtils
import com.bumptech.glide.Glide
import com.hjq.shape.layout.ShapeConstraintLayout
import com.hjq.toast.ToastUtils
import com.ttsq.mobile.R
import com.ttsq.mobile.app.AppAdapter
import com.ttsq.mobile.http.api.PyqGoodsApi
import com.ttsq.mobile.ui.activity.BrowserActivity
import com.ttsq.mobile.ui.activity.GoodsDetailActivity

/**
 * 朋友圈
 */
class PqyGoodsAdapter(val mContext: Context) : AppAdapter<PyqGoodsApi.PyqGoodsDto>(mContext) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.item_pyq), BGANinePhotoLayout.Delegate {

        private val goods_info: ShapeConstraintLayout? by lazy { findViewById(R.id.goods_info) }
        private val copy_wenan: TextView? by lazy { findViewById(R.id.copy_wenan) }
        private val textView3: TextView? by lazy { findViewById(R.id.textView3) }
        private val textView4: TextView? by lazy { findViewById(R.id.textView4) }
        private val textView6: TextView? by lazy { findViewById(R.id.textView6) }
        private val textView7: TextView? by lazy { findViewById(R.id.textView7) }
        private val textView8: TextView? by lazy { findViewById(R.id.textView8) }
        private val textView9: TextView? by lazy { findViewById(R.id.textView9) }
        private val nine_img_view: BGANinePhotoLayout? by lazy { findViewById(R.id.nine_img_view) }
        private val goods_pic: ImageView? by lazy { findViewById(R.id.goods_pic) }

        override fun onBindView(position: Int) {
            val pyqGoodsDto = getItem(position)

            textView3?.text =
                TimeUtils.millis2String(pyqGoodsDto.add_time * 1000, "yyyy.mm.dd hh:mm")
            textView6?.text = pyqGoodsDto.content
            textView7?.text = pyqGoodsDto.itemtitle
            textView8?.text = "￥${pyqGoodsDto.itemendprice}"
            textView9?.text = "￥${pyqGoodsDto.itemprice}"

            goods_pic?.let { Glide.with(mContext).load(pyqGoodsDto.itempic[0]).into(it) }

            nine_img_view
            nine_img_view?.setDelegate(this)
            nine_img_view?.data = pyqGoodsDto.itempic

            textView4?.setOnClickListener {
                GoodsDetailActivity.start(mContext, pyqGoodsDto.couponurl)
            }

            copy_wenan?.setOnClickListener {
                val cm =
                    it.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                // 创建普通字符型ClipData
                val mClipData = ClipData.newPlainText("Label", pyqGoodsDto.copy_content)
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData)
                ToastUtils.show("文案复制成功")
            }
            goods_info?.setOnClickListener {
                GoodsDetailActivity.start(mContext, pyqGoodsDto.itemid)
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