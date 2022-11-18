package com.easybuy.mobile.ui.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout
import com.blankj.utilcode.util.AppUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.easybug.mobile.R
import com.easybuy.mobile.app.AppAdapter
import com.easybuy.mobile.http.api.GetLingquanUrlApi
import com.easybuy.mobile.http.api.GetTaoKayApi
import com.easybuy.mobile.http.api.GetWeChatImgApi
import com.easybuy.mobile.http.api.YqbkApi
import com.easybuy.mobile.http.model.HttpData
import com.easybuy.mobile.ui.activity.BrowserActivity
import com.easybuy.mobile.ui.activity.GoodsDetailActivity
import com.hjq.http.EasyHttp
import com.hjq.http.lifecycle.ApplicationLifecycle
import com.hjq.http.listener.OnHttpListener
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
        private val yuanjia: TextView? by lazy { findViewById(R.id.yuanjia) }
        private val ljlq_btn: TextView? by lazy { findViewById(R.id.ljlq_btn) }
        private val wa: TextView? by lazy { findViewById(R.id.wa) }
        private val nine_img_view: BGANinePhotoLayout? by lazy { findViewById(R.id.nine_img_view) }

        private val quanhoujia: TextView? by lazy { findViewById(R.id.quanhoujia) }
        private val save_img: LinearLayout? by lazy { findViewById(R.id.save_img) }
        private val copy_kl: LinearLayout? by lazy { findViewById(R.id.copy_kl) }
        private val tv_copy_wa: TextView? by lazy { findViewById(R.id.tv_copy_wa) }
        private val goods_price: TextView? by lazy { findViewById(R.id.goods_price) }
        private val tv_sheng: TextView? by lazy { findViewById(R.id.tv_sheng) }

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
            yuanjia?.text = "${goodsInfo.size}"
            goods_price?.text = "${goodsInfo.quanhou_jiage}"
            tv_sheng?.text = "立省${goodsInfo.coupon_info_money}"

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

            ljlq_btn?.setOnClickListener {
                getGaoyongUrl(goodsInfo.tao_id,1)
            }

            //复制口令
            copy_kl?.setOnClickListener {
                getGaoyongUrl(goodsInfo.tao_id,2)
            }

            //保存图片
            save_img?.setOnClickListener {
                getGaoyongUrl(goodsInfo.tao_id,3)
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

    private fun getImgUrl(taoKey: String) {
        EasyHttp.get(ApplicationLifecycle())
            .api(GetWeChatImgApi().apply {
                content = taoKey
            })
            .request(object :
                OnHttpListener<HttpData<String>> {
                override fun onSucceed(result: HttpData<String>?) {
                    //TODO 分享逻辑未做
                    ToastUtils.show("图片已生成,分享到微信:${result?.getData()}")
                }

                override fun onFail(e: java.lang.Exception?) {
                    ToastUtils.show(e?.message)
                }

            })
    }

    /**
     * 获取高佣链接
     * @param type : 1,跳转淘宝  2:复制口令  3:分享图片
     */
    private fun getGaoyongUrl(taoId: String,type:Int) {
        EasyHttp.get(ApplicationLifecycle())
            .api(GetLingquanUrlApi().apply {
                num_iid = taoId
            })
            .request(object :
                OnHttpListener<HttpData<ArrayList<GetLingquanUrlApi.LingquanUrlBean>>> {
                override fun onSucceed(result: HttpData<ArrayList<GetLingquanUrlApi.LingquanUrlBean>>?) {
                    val url = result?.getData()?.get(0)?.coupon_click_url.toString()
                    if (type==1){
                        if (AppUtils.isAppInstalled("com.taobao.taobao")) {
                            val intent = Intent()
                            intent.setAction("Android.intent.action.VIEW");
                            val uri = Uri.parse(
                                result?.getData()?.get(0)?.coupon_click_url.toString()
                            ); // 商品地址
                            intent.setData(uri);
                            intent.setClassName(
                                "com.taobao.taobao",
                                "com.taobao.browser.BrowserActivity"
                            );
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//在非activity类中调用startactivity方法必须添加标签
                            mContext.startActivity(intent)
                        } else {
                            BrowserActivity.start(
                                mContext,
                                result?.getData()?.get(0)?.coupon_click_url.toString()
                            )
                        }
                    }else{
                        getTaoKey(url,type)
                    }
                }

                override fun onFail(e: java.lang.Exception?) {
                    ToastUtils.show(e?.message)
                }

            })
    }

    /**
     * 获取淘口令信息
     *      * @param type : 1,跳转淘宝  2:复制口令  3:分享图片
     */
    private fun getTaoKey(url: String, type: Int) {
        EasyHttp.get(ApplicationLifecycle())
            .api(GetTaoKayApi().apply {
                this.url = url
            })
            .request(object : OnHttpListener<HttpData<String>> {
                override fun onSucceed(result: HttpData<String>?) {
                    val taoKeyStr = result?.getData()
                    if (type==2) {
                        val cm =
                            mContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        // 创建普通字符型ClipData
                        val mClipData = ClipData.newPlainText(
                            "Label",
                            taoKeyStr
                        )
                        // 将ClipData内容放到系统剪贴板里。
                        cm.setPrimaryClip(mClipData)
                        ToastUtils.show("淘口令复制成功")
                    }else{
                        if (taoKeyStr != null) {
                            getImgUrl(taoKeyStr)
                        }else{
                            ToastUtils.show("淘口令获取失败")
                        }
                    }
                }

                override fun onFail(e: Exception?) {
                    ToastUtils.show(e?.message)
                }

            })
    }
}