package com.ttsq.mobile.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.TimeUtils
import com.bumptech.glide.Glide
import com.bytedance.sdk.openadsdk.AdSlot
import com.bytedance.sdk.openadsdk.TTAdDislike
import com.bytedance.sdk.openadsdk.TTAdNative
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.bytedance.sdk.openadsdk.TTFeedAd
import com.bytedance.sdk.openadsdk.TTNativeExpressAd
import com.bytedance.sdk.openadsdk.mediation.ad.MediationExpressRenderListener
import com.hjq.base.BaseDialog
import com.ttsq.mobile.R
import com.ttsq.mobile.aop.Log
import com.ttsq.mobile.aop.SingleClick
import com.ttsq.mobile.app.AppActivity
import com.ttsq.mobile.http.api.GetLingquanUrlApi
import com.ttsq.mobile.http.api.GoodsDetailApi
import com.ttsq.mobile.http.api.HomeGoodsListApi
import com.ttsq.mobile.http.api.SameClassGoodsApi
import com.ttsq.mobile.http.glide.GlideApp
import com.ttsq.mobile.http.model.HttpData
import com.ttsq.mobile.ui.adapter.GoodsDetailBannerAdapter
import com.ttsq.mobile.ui.adapter.SearchGoodsListAdapter
import com.ttsq.mobile.ui.dialog.ShareDialog
import com.ttsq.mobile.utils.FormatUtils
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.orhanobut.logger.Logger
import com.pdlbox.tools.utils.ConversionUtils
import com.ttsq.mobile.http.model.AdDto
import com.ttsq.mobile.http.model.DataType
import com.ttsq.mobile.http.model.GoodsDetailDto
import com.ttsq.mobile.manager.UserManager
import com.ttsq.mobile.other.GridSpacingItemDecoration
import com.ttsq.mobile.other.PermissionInterceptor
import com.ttsq.mobile.ui.dialog.MessageDialog
import com.ttsq.mobile.ui.fragment.HomeFragment
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.media.UMWeb
import com.youth.banner.Banner
import com.youth.banner.listener.OnPageChangeListener

/**
 * @project : EasyBuy_Android
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/6/23
 */
class GoodsDetailActivity : AppActivity() {


    companion object {

        private const val GOODS_ID: String = "GOODS_ID"
        private const val ZTK_CODE: String = "ZTK_CODE"

        @Log
        fun start(mContext: Context, goodsId: String?, ztkCode: String = "") {
            val intent = Intent(mContext, GoodsDetailActivity::class.java)
            intent.putExtra(GOODS_ID, goodsId)
            intent.putExtra(ZTK_CODE, ztkCode)
            mContext.startActivity(intent)
        }
    }

    private var goodsListAdapter: SearchGoodsListAdapter? = null
    private val goods_price: TextView? by lazy { findViewById<TextView>(R.id.goods_price) }
    private val goods_title: TextView? by lazy { findViewById<TextView>(R.id.goods_title) }
    private val yuanjia: TextView? by lazy { findViewById<TextView>(R.id.yuanjia) }
    private val buy_num: TextView? by lazy { findViewById<TextView>(R.id.buy_num) }
    private val home: LinearLayout? by lazy { findViewById<LinearLayout>(R.id.home) }
    private val shop_name: TextView? by lazy { findViewById<TextView>(R.id.shop_name) }
    private val num_bbms: TextView? by lazy { findViewById<TextView>(R.id.num_bbms) }
    private val num_mjfw: TextView? by lazy { findViewById<TextView>(R.id.num_mjfw) }
    private val num_wlfw: TextView? by lazy { findViewById<TextView>(R.id.num_wlfw) }
    private val yh_str: TextView? by lazy { findViewById<TextView>(R.id.yh_str) }
    private val yhq_jine: TextView? by lazy { findViewById<TextView>(R.id.yhq_jine) }
    private val start_time: TextView? by lazy { findViewById<TextView>(R.id.start_time) }
    private val end_time: TextView? by lazy { findViewById<TextView>(R.id.end_time) }
    private val banner_indector: TextView? by lazy { findViewById<TextView>(R.id.banner_indector) }
    private val yhje_str: TextView? by lazy { findViewById<TextView>(R.id.yhje_str) }
    private val tvLq: TextView? by lazy { findViewById<TextView>(R.id.tv_lq) }
    private val llYhq: ConstraintLayout? by lazy { findViewById<ConstraintLayout>(R.id.ll_yhq) }
    private val llYh: LinearLayout? by lazy { findViewById<LinearLayout>(R.id.ll_yh) }
    private val yhStr: TextView? by lazy { findViewById<TextView>(R.id.yh_str) }
    private val shop_logo: ImageView? by lazy { findViewById<ImageView>(R.id.shop_logo) }
    private val iv_back2: ImageView? by lazy { findViewById<ImageView>(R.id.iv_back2) }
    private val xqtList: LinearLayout? by lazy { findViewById<LinearLayout>(R.id.xqt_list) }
    private val ll_lq: LinearLayout? by lazy { findViewById<LinearLayout>(R.id.ll_lq) }
    private val iv_lq: TextView? by lazy { findViewById(R.id.iv_lq) }
    private val tv_fan_money: TextView? by lazy { findViewById(R.id.tv_fan_money) }
    private val ll_share: LinearLayout? by lazy { findViewById(R.id.ll_share) }
    private val goodsList: RecyclerView? by lazy { findViewById<RecyclerView>(R.id.goods_list) }
    private val banner: Banner<String, GoodsDetailBannerAdapter>? by lazy { findViewById(R.id.goods_banner) }
    private val bannerContainer: FrameLayout? by lazy { findViewById(R.id.ad_view) }

    //@[classname]
    private var bannerAd: TTNativeExpressAd? = null

    //@[classname]
    private var adNativeLoader: TTAdNative? = null

    //@[classname]
    private var adSlot: AdSlot? = null

    //@[classname]
    private var nativeExpressAdListener: TTAdNative.NativeExpressAdListener? = null

    //@[classname]
    private var expressAdInteractionListener: TTNativeExpressAd.ExpressAdInteractionListener? = null

    //@[classname]
    private var dislikeInteractionCallback: TTAdDislike.DislikeInteractionCallback? = null
    private var mFeedAdListener: TTAdNative.FeedAdListener? = null // 广告加载监听器

    override fun getLayoutId(): Int {
        return R.layout.activity_goods_detail
    }

    override fun initView() {
        setOnClickListener(iv_back2, ll_lq, iv_lq, ll_share, home)
        banner?.let {
//            it.setBannerGalleryEffect(39, 16)
            it.addBannerLifecycleObserver(this)
            it.addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int,
                ) {

                }

                override fun onPageSelected(position: Int) {
                    banner_indector?.text = "${position + 1}/${bannerList}"
                }

                override fun onPageScrollStateChanged(state: Int) {

                }

            })
        }

        goodsList?.let {
            it.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            goodsListAdapter = SearchGoodsListAdapter(this)
            it.adapter = goodsListAdapter
            it.addItemDecoration(
                GridSpacingItemDecoration(
                    2,
                    ConversionUtils.dp2Px(10f),
                    false
                )
            )
        }
    }

    override fun initData() {
        loadAd()
        EasyHttp.get(this)
            .api(GoodsDetailApi().apply {
                itemid = getString(GOODS_ID).toString()
            })
            .request(object : OnHttpListener<HttpData<GoodsDetailDto>> {
                override fun onSucceed(result: HttpData<GoodsDetailDto>?) {
                    initGoodsInfo(result?.getData())
                    getSameClassGoods(result?.getData()?.itemid)
                }

                override fun onFail(e: Exception?) {
                    toast(e?.message)
                }
            })
    }

    /**
     * 获取相似商品
     */
    private fun getSameClassGoods(taoId: String?) {
        EasyHttp.get(this)
            .api(SameClassGoodsApi().apply {
                itemid = taoId.toString()
            })
            .request(object : OnHttpListener<HttpData<ArrayList<GoodsDetailDto>>> {
                override fun onSucceed(result: HttpData<ArrayList<GoodsDetailDto>>?) {
                    val tempData = arrayListOf<AdDto>()
                    result?.getData()?.forEachIndexed { index, goodsDetailDto ->
                        tempData.add(AdDto(DataType.DATE, goodsDetailDto))
                    }
                    goodsListAdapter?.addData(tempData)
                    loadFeedAd()
                }

                override fun onFail(e: java.lang.Exception?) {
                    toast(e?.message)
                }
            })
    }

    @SingleClick
    override fun onClick(view: View) {
        when (view) {
            home -> {
                HomeActivity.start(this, HomeFragment::class.java)
            }

            iv_back2 -> {
                finish()
            }

            iv_lq, ll_lq -> {
                //判断是否授权绑定
                UserManager.userInfo?.let {
                    if (it.taobaoSpecialId.isNullOrBlank()) {
                        MessageDialog.Builder(this)
                            .setTitle("温馨提示")
                            .setMessage("授权绑定淘宝，可以获得返利权益，是否立即绑定？")
                            .setConfirm("立即绑定")
                            .setCancel("直接购买")
                            .setListener(object : MessageDialog.OnListener {
                                override fun onConfirm(dialog: BaseDialog?) {
                                    startActivity(AuthorizationManagementActivity::class.java)
                                }

                                override fun onCancel(dialog: BaseDialog?) {
                                    getLingquanUrl()
                                }
                            })
                            .show()
                    }else{
                        getLingquanUrl()
                    }
                }
            }

            ll_share -> {
                getLingquanUrl(needShare = true)
            }

            else -> {}
        }
    }

    /**
     * 获取领券地址
     */
    private fun getLingquanUrl(needShare: Boolean = false) {
        showDialog()
        EasyHttp.post(this)
            .api(GetLingquanUrlApi().apply {
                itemid = getString(GOODS_ID).toString()
                title = goodsInfoData?.itemtitle.toString()
            })
            .request(object :
                OnHttpListener<HttpData<GetLingquanUrlApi.LingquanUrlDto>> {
                override fun onSucceed(result: HttpData<GetLingquanUrlApi.LingquanUrlDto>?) {
                    hideDialog()
                    if (needShare) {
                        XXPermissions.with(this@GoodsDetailActivity)
                            .interceptor(PermissionInterceptor("使用存储权限,用于分享链接/图片等到QQ/微信等平台"))
                            .permission(Permission.WRITE_EXTERNAL_STORAGE)
                            .request { _, all ->
                                if (all) {
                                    val umWeb = UMWeb(
                                        result?.getData()?.coupon_click_url.toString()
                                    )
                                    umWeb.title = "粉丝福利购"
                                    umWeb.description = "点击领取福利"
                                    ShareDialog.Builder(this@GoodsDetailActivity)
                                        .setShareLink(umWeb)
                                        .show()
                                }
                            }

                        return
                    }

                    BrowserActivity.start(
                        this@GoodsDetailActivity,
                        result?.getData()?.coupon_click_url.toString()
                    )
                }

                override fun onFail(e: java.lang.Exception?) {
                    hideDialog()
                    toast(e?.message)
                }

            })
    }

    private var bannerList = 1

    private var goodsInfoData: GoodsDetailDto? = null

    /**
     * 设置商品信息
     */
    private fun initGoodsInfo(goodsInfo: GoodsDetailDto?) {
        goodsInfoData = goodsInfo
        goods_title?.text = goodsInfo?.itemtitle
        goods_price?.text = goodsInfo?.itemendprice
        yh_str?.text = "省${goodsInfo?.couponmoney}元"
        yhq_jine?.text = goodsInfo?.couponmoney
        tv_fan_money?.text = goodsInfo?.tkmoney
        start_time?.text = "${
            goodsInfo?.couponstarttime?.let {
                TimeUtils.millis2String(
                    it * 1000,
                    "yyyy.mm.dd hh:mm"
                )
            }
        }"
        end_time?.text = "${
            goodsInfo?.couponendtime?.let {
                TimeUtils.millis2String(
                    it * 1000,
                    "yyyy.mm.dd hh:mm"
                )
            }
        }"
        yhje_str?.text = goodsInfo?.couponinfo
        yuanjia?.text = goodsInfo?.itemprice
        yuanjia?.paint?.flags = Paint.STRIKE_THRU_TEXT_FLAG
        FormatUtils.formatSales(buy_num, goodsInfo?.itemsale)
//        shop_logo?.let { GlideApp.with(this).load(goodsInfo?.shopIcon).into(it) }
//        shop_name?.text = goodsInfo?.shop_title
//        num_bbms?.text = goodsInfo?.score1
//        num_mjfw?.text = goodsInfo?.score2
//        num_wlfw?.text = goodsInfo?.score3

        val imgList = ArrayList<String>()
        goodsInfo?.taobao_image?.split(",")?.forEachIndexed { index, s ->
            imgList.add("${s}")
            val imageView = ImageView(this)
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            imageView.layoutParams = params
            imageView.adjustViewBounds = true
            Glide.with(this@GoodsDetailActivity).load(s).into(imageView)
            xqtList?.addView(imageView)
            if (index > 10) {
                //FIXME 这里暂时设置最长的详情图为10,解决详情卡顿的问题
                return
            }
        }
        bannerList = imgList.size
        banner?.setAdapter(GoodsDetailBannerAdapter(imgList))

        if (goodsInfo?.couponmoney == "0" || goodsInfo?.couponmoney == "0.0" || goodsInfo?.couponmoney == "0.00") {
            llYhq?.visibility = View.GONE
            llYh?.visibility = View.GONE
            yhStr?.visibility = View.GONE
            yuanjia?.visibility = View.GONE
            tvLq?.text = "立即购买"
        } else {
            llYhq?.visibility = View.VISIBLE
            llYh?.visibility = View.VISIBLE
            yhStr?.visibility = View.VISIBLE
            yuanjia?.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
    }

    private fun loadAd() {
        bannerContainer?.removeAllViews()

        /** 1、创建AdSlot对象 */
        //@[classname]
        adSlot = AdSlot.Builder()
            .setCodeId("102652446")
            .setImageAcceptedSize(ScreenUtils.getScreenWidth(), ConvertUtils.dp2px(150f)) // 单位px
            .build()

        /** 2、创建TTAdNative对象 */
        //@[classname]//@[methodname]
        adNativeLoader = TTAdSdk.getAdManager().createAdNative(this)

        /** 3、创建加载、展示监听器 */
        initListeners()

        /** 4、加载广告 */
        adNativeLoader?.loadBannerExpressAd(adSlot, nativeExpressAdListener)
    }

    private fun showAd() {
        if (bannerAd == null) {
            Logger.d("请先加载广告或等待广告加载完毕后再调用show方法")
        }
        bannerAd?.setExpressInteractionListener(expressAdInteractionListener)
        bannerAd?.setDislikeCallback(this@GoodsDetailActivity, dislikeInteractionCallback)

        /** 注意：使用融合功能时，load成功后可直接调用getExpressAdView获取广告view展示，而无需调用render等onRenderSuccess后 */
        val bannerView: View? = bannerAd?.expressAdView
        if (bannerView != null) {
            bannerContainer?.removeAllViews()
            bannerContainer?.visibility = View.VISIBLE
            bannerContainer?.addView(bannerView)
        }
    }

    private fun initListeners() {
        // 广告加载监听器
        mFeedAdListener = object : TTAdNative.FeedAdListener {
            override fun onError(code: Int, message: String) {
                Logger.d("onError: $code, $message")
            }

            override fun onFeedAdLoad(ads: List<TTFeedAd>) {
                if (ads == null || ads.isEmpty()) {
                    Logger.d("on FeedAdLoaded: ad is null!")
                    return
                }
                for (ad in ads) {
                    /** 5、加载成功后，添加到RecyclerView中展示广告  */
                    if (ad != null) {
                        val manager = ad.mediationManager
                        if (manager != null && manager.isExpress) {
                            ad.setExpressRenderListener(object : MediationExpressRenderListener {
                                override fun onRenderFail(view: View, s: String, i: Int) {
                                    Logger.d("feed express render fail, errCode: $i, errMsg: $s")
                                }

                                override fun onAdClick() {
                                    Logger.d("feed express click")
                                }

                                override fun onAdShow() {
                                    Logger.d("feed express show")
                                }

                                override fun onRenderSuccess(
                                    view: View?,
                                    v: Float,
                                    v1: Float,
                                    b: Boolean
                                ) {
                                    // 模板广告在renderSuccess后，添加到ListView中展示
                                    Logger.d("onRenderSuccess: ${v} ${v1} ${b}}")
                                    goodsListAdapter?.let {
                                        var i = it.getCount() - 5
                                        if (i < 0) {
                                            i = it.getCount()
                                        }
                                        it.addItem(i, AdDto(DataType.AD, ads[0]))
                                    }
                                }
                            })
                            ad.render() // 调用render方法进行渲染
                        }
                    }
                }
            }
        }
        //@[classname]
        nativeExpressAdListener = object : TTAdNative.NativeExpressAdListener {
            //@[classname]
            override fun onNativeExpressAdLoad(ads: MutableList<TTNativeExpressAd>?) {
                if (ads != null) {
                    Logger.d("banner load success: " + ads.size)
                }
                ads?.let {
                    if (it.size > 0) {
                        //@[classname]
                        val ad: TTNativeExpressAd = it[0]
                        bannerAd = ad
                    }
                }
                showAd()
            }

            override fun onError(code: Int, message: String?) {
                Logger.d("banner load fail: $code, $message")
            }
        }
        // 广告展示监听器
        expressAdInteractionListener = object :
        //@[classname]
            TTNativeExpressAd.ExpressAdInteractionListener {
            override fun onAdClicked(view: View?, type: Int) {
                Logger.d("banner clicked")
            }

            override fun onAdShow(view: View?, type: Int) {
                Logger.d("banner show")
            }

            override fun onRenderFail(view: View?, msg: String?, code: Int) {
                // 注意：使用融合功能时，无需调用render，load成功后可调用mBannerAd.getExpressAdView()进行展示。
            }

            override fun onRenderSuccess(view: View?, width: Float, height: Float) {
                // 注意：使用融合功能时，无需调用render，load成功后可调用mBannerAd.getExpressAdView()获取view进行展示。
                // 如果调用了render，则会直接回调onRenderSuccess，***** 参数view为null，请勿使用。*****
            }
        }

        // dislike监听器，广告关闭时会回调onSelected
        //@[classname]
        dislikeInteractionCallback = object : TTAdDislike.DislikeInteractionCallback {
            override fun onShow() {
                Logger.d("banner dislike show")
            }

            override fun onSelected(
                position: Int,
                value: String?,
                enforce: Boolean
            ) {
                bannerContainer?.visibility = View.GONE
                Logger.d("banner dislike closed")
                bannerContainer?.removeAllViews()
            }

            override fun onCancel() {
                Logger.d("banner dislike cancel")
            }
        }
    }

    private fun loadFeedAd() {
        /** 1、创建AdSlot对象  */
        val adSlot = AdSlot.Builder()
            .setCodeId("102650196")
            .setExpressViewAcceptedSize(
                ConvertUtils.px2dp((ScreenUtils.getScreenWidth() / 2).toFloat()).toFloat(),
                0f
            )
            .setAdCount(1) // 请求广告数量为1到3条 （优先采用平台配置的数量）
            .build()

        /** 2、创建TTAdNative对象  */
        val adNativeLoader: TTAdNative = TTAdSdk.getAdManager().createAdNative(this)
        /** 3、创建加载、展示监听器  */
        initListeners()
        /** 4、加载广告  */
        adNativeLoader.loadFeedAd(adSlot, mFeedAdListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        bannerAd?.destroy()
        /** 6、在onDestroy中销毁广告  */
        val mData = goodsListAdapter?.getData()
        if (mData != null) {
            for (itemData in mData) {
                if (itemData.type == DataType.AD) {
                    (itemData.data as TTFeedAd).destroy()
                }
            }
        }
    }
}