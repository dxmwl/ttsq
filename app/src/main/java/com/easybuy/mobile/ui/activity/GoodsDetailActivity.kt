package com.easybuy.mobile.ui.activity

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.easybug.mobile.R
import com.easybuy.mobile.aop.Log
import com.easybuy.mobile.app.AppActivity
import com.easybuy.mobile.http.api.GoodsDetailApi
import com.easybuy.mobile.http.api.HomeGoodsListApi
import com.easybuy.mobile.http.glide.GlideApp
import com.easybuy.mobile.http.model.HttpData
import com.easybuy.mobile.ui.adapter.GoodsDetailBannerAdapter
import com.easybuy.mobile.utils.FormatUtils
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.youth.banner.Banner

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
        fun start(mContext: Context, goodsId: String?, ztkCode: String?) {
            val intent = Intent(mContext, GoodsDetailActivity::class.java)
            intent.putExtra(GOODS_ID, goodsId)
            intent.putExtra(ZTK_CODE, ztkCode)
            mContext.startActivity(intent)
        }
    }

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
    private val yhje_str: TextView? by lazy { findViewById<TextView>(R.id.yhje_str) }
    private val tvLq: TextView? by lazy { findViewById<TextView>(R.id.tv_lq) }
    private val llYhq: LinearLayout? by lazy { findViewById<LinearLayout>(R.id.ll_yhq) }
    private val llYh: LinearLayout? by lazy { findViewById<LinearLayout>(R.id.ll_yh) }
    private val yhStr: TextView? by lazy { findViewById<TextView>(R.id.yh_str) }
    private val shop_logo: ImageView? by lazy { findViewById<ImageView>(R.id.shop_logo) }
    private val xqtList: LinearLayout? by lazy { findViewById<LinearLayout>(R.id.xqt_list) }
    private val banner: Banner<String, GoodsDetailBannerAdapter>? by lazy { findViewById(R.id.goods_banner) }

    override fun getLayoutId(): Int {
        return R.layout.activity_goods_detail
    }

    override fun initView() {
        banner?.let {
//            it.setBannerGalleryEffect(39, 16)
            it.addBannerLifecycleObserver(this)
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
                }

                override fun onFail(e: Exception?) {
                    toast(e?.message)
                }
            })
    }

    /**
     * 设置商品信息
     */
    private fun initGoodsInfo(goodsInfo: HomeGoodsListApi.GoodsBean?) {
        goods_title?.text = goodsInfo?.title
        goods_price?.text = goodsInfo?.quanhou_jiage
        yh_str?.text = "省${goodsInfo?.coupon_info_money}元"
        yhq_jine?.text = goodsInfo?.coupon_info_money
        yhje_str?.text = goodsInfo?.coupon_info
        yuanjia?.text = goodsInfo?.size
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