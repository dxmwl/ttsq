package com.shengqianjun.mobile.ui.activity

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
import com.bumptech.glide.Glide
import com.shengqianjun.mobile.R
import com.shengqianjun.mobile.aop.Log
import com.shengqianjun.mobile.aop.SingleClick
import com.shengqianjun.mobile.app.AppActivity
import com.shengqianjun.mobile.http.api.GetLingquanUrlApi
import com.shengqianjun.mobile.http.api.GoodsDetailApi
import com.shengqianjun.mobile.http.api.HomeGoodsListApi
import com.shengqianjun.mobile.http.api.SameClassGoodsApi
import com.shengqianjun.mobile.http.glide.GlideApp
import com.shengqianjun.mobile.http.model.HttpData
import com.shengqianjun.mobile.ui.adapter.GoodsDetailBannerAdapter
import com.shengqianjun.mobile.ui.adapter.SearchGoodsListAdapter
import com.shengqianjun.mobile.ui.dialog.ShareDialog
import com.shengqianjun.mobile.utils.FormatUtils
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
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
        fun start(mContext: Context, goodsId: String?, ztkCode: String ="") {
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
        setOnClickListener(iv_back2, ll_lq, iv_lq, ll_share)
        banner?.let {
//            it.setBannerGalleryEffect(39, 16)
            it.addBannerLifecycleObserver(this)
            it.addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
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
                tao_id = getString(GOODS_ID).toString()
                code = getString(ZTK_CODE).toString()
            })
            .request(object : OnHttpListener<HttpData<ArrayList<HomeGoodsListApi.GoodsBean>>> {
                override fun onSucceed(result: HttpData<ArrayList<HomeGoodsListApi.GoodsBean>>?) {
                    initGoodsInfo(result?.getData()?.get(0))
                    getSameClassGoods(result?.getData()?.get(0)?.tao_id)
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
                item_id = taoId.toString()
            })
            .request(object : OnHttpListener<HttpData<ArrayList<HomeGoodsListApi.GoodsBean>>> {
                override fun onSucceed(result: HttpData<ArrayList<HomeGoodsListApi.GoodsBean>>?) {
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
        EasyHttp.get(this)
            .api(GetLingquanUrlApi().apply {
                num_iid = getString(GOODS_ID).toString()
            })
            .request(object :
                OnHttpListener<HttpData<ArrayList<GetLingquanUrlApi.LingquanUrlBean>>> {
                override fun onSucceed(result: HttpData<ArrayList<GetLingquanUrlApi.LingquanUrlBean>>?) {
                    if (needShare) {
                        ShareDialog.Builder(this@GoodsDetailActivity)
                            .setShareLink(
                                UMWeb(
                                    result?.getData()?.get(0)?.coupon_click_url.toString()
                                )
                            )
                            .show()
                        return
                    }

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
                        startActivity(intent)
                    } else {
                        BrowserActivity.start(
                            this@GoodsDetailActivity,
                            result?.getData()?.get(0)?.coupon_click_url.toString()
                        )
                    }
                }

                override fun onFail(e: java.lang.Exception?) {
                    toast(e?.message)
                }

            })
    }

    private var bannerList = 1

    /**
     * 设置商品信息
     */
    private fun initGoodsInfo(goodsInfo: HomeGoodsListApi.GoodsBean?) {
        goods_title?.text = goodsInfo?.title
        goods_price?.text = goodsInfo?.quanhou_jiage
        yh_str?.text = "省${goodsInfo?.coupon_info_money}元"
        yhq_jine?.text = goodsInfo?.coupon_info_money
        start_time?.text = "${goodsInfo?.coupon_start_time}"
        end_time?.text = "${goodsInfo?.coupon_end_time}"
        yhje_str?.text = goodsInfo?.coupon_info
        yuanjia?.text = goodsInfo?.size
        yuanjia?.paint?.flags = Paint.STRIKE_THRU_TEXT_FLAG
        FormatUtils.formatSales(buy_num, goodsInfo?.volume)
        shop_logo?.let { GlideApp.with(this).load(goodsInfo?.shopIcon).into(it) }
        shop_name?.text = goodsInfo?.shop_title
        num_bbms?.text = goodsInfo?.score1
        num_mjfw?.text = goodsInfo?.score2
        num_wlfw?.text = goodsInfo?.score3

        val imgList = ArrayList<String>()
        goodsInfo?.small_images?.split("|")?.forEachIndexed { index, s ->
            imgList.add(s)
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

        if (goodsInfo?.coupon_info_money == "0" || goodsInfo?.coupon_info_money == "0.0" || goodsInfo?.coupon_info_money == "0.00") {
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
}