package com.ttsq.mobile.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.TimeUtils
import com.bumptech.glide.Glide
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
import com.ttsq.mobile.http.model.GoodsDetailDto
import com.ttsq.mobile.other.PermissionInterceptor
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

    private lateinit var goodsListAdapter: SearchGoodsListAdapter
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
    private val ll_share: LinearLayout? by lazy { findViewById(R.id.ll_share) }
    private val goodsList: RecyclerView? by lazy { findViewById<RecyclerView>(R.id.goods_list) }
    private val banner: Banner<String, GoodsDetailBannerAdapter>? by lazy { findViewById(R.id.goods_banner) }

    override fun getLayoutId(): Int {
        return R.layout.activity_goods_detail
    }

    override fun initView() {
        setOnClickListener(iv_back2, ll_lq, iv_lq, ll_share,home)
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
            it.layoutManager = GridLayoutManager(this, 2)
            goodsListAdapter = SearchGoodsListAdapter(this)
            it.adapter = goodsListAdapter
        }
    }

    override fun initData() {

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
                    goodsListAdapter.addData(result?.getData())
                }

                override fun onFail(e: java.lang.Exception?) {
                    toast(e?.message)
                }
            })
    }

    @SingleClick
    override fun onClick(view: View) {
        when (view) {
            home->{
                HomeActivity.start(this,HomeFragment::class.java)
            }
            iv_back2 -> {
                finish()
            }
            iv_lq, ll_lq -> {
                getLingquanUrl()
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
                            .interceptor(PermissionInterceptor())
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

                    if (AppUtils.isAppInstalled("com.taobao.taobao")) {
                        val intent = Intent()
                        intent.setAction("Android.intent.action.VIEW");
                        val uri = Uri.parse(
                            result?.getData()?.coupon_click_url.toString()
                        ); // 商品地址
                        intent.setData(uri);
                        intent.setClassName(
                            "com.taobao.taobao",
                            "com.taobao.browser.BrowserActivity"
                        );
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//在非activity类中调用startactivity方法必须添加标签
                        startActivity(intent)
                    } else {
                        BrowserActivity.start(
                            this@GoodsDetailActivity,
                            result?.getData()?.coupon_click_url.toString()
                        )
                    }
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
}