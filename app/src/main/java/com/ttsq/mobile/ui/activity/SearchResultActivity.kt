package com.ttsq.mobile.ui.activity

import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ScreenUtils
import com.bytedance.sdk.openadsdk.AdSlot
import com.bytedance.sdk.openadsdk.TTAdNative
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.bytedance.sdk.openadsdk.TTFeedAd
import com.bytedance.sdk.openadsdk.mediation.ad.MediationExpressRenderListener
import com.ttsq.mobile.R
import com.ttsq.mobile.aop.SingleClick
import com.ttsq.mobile.app.AppActivity
import com.ttsq.mobile.app.AppHelper
import com.ttsq.mobile.http.api.HomeGoodsListApi
import com.ttsq.mobile.http.api.SearchGoodsApi
import com.ttsq.mobile.http.model.HttpData
import com.ttsq.mobile.http.model.MenuDto
import com.ttsq.mobile.ui.adapter.SearchGoodsListAdapter
import com.ttsq.mobile.ui.popup.ZongheShadowPopupView
import com.google.android.material.tabs.TabLayout
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.hjq.shape.view.ShapeCheckBox
import com.orhanobut.logger.Logger
import com.pdlbox.tools.utils.ConversionUtils
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.ttsq.mobile.http.model.AdDto
import com.ttsq.mobile.http.model.DataType
import com.ttsq.mobile.http.model.GoodsDetailDto
import com.ttsq.mobile.other.GridSpacingItemDecoration

/**
 * @project : EasyBuy_Android
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/6/23
 */
class SearchResultActivity : AppActivity() {
    private var pageIndex: Int = 1
    private var keywordStr: String = ""

    private var homeGoodsListAdapter: SearchGoodsListAdapter? = null
    private val goodsList: RecyclerView? by lazy { findViewById(R.id.goods_list) }
    private val refresh: SmartRefreshLayout? by lazy { findViewById(R.id.refresh) }
    private val input_keyword: EditText? by lazy { findViewById(R.id.input_keyword) }
    private val sort_view: TextView? by lazy { findViewById(R.id.sort_view) }
    private val is_youquan: ShapeCheckBox? by lazy { findViewById(R.id.is_youquan) }
    private val goods_sort: TabLayout? by lazy { findViewById(R.id.goods_sort) }
    private var zonghePopupView: ZongheShadowPopupView? = null
    private var isYouquan: Int = 1
    private var paixu: String = "0"
    private var mFeedAdListener: TTAdNative.FeedAdListener? = null // 广告加载监听器

    override fun getLayoutId(): Int {
        return R.layout.activity_search_result
    }

    override fun initView() {
        setOnClickListener(R.id.btn_search, R.id.iv_back, R.id.sort_view)
        keywordStr = intent.getStringExtra("KEYWORD").toString()
        input_keyword?.setText(keywordStr)
        input_keyword?.setSelection(keywordStr.length)

        input_keyword?.addTextChangedListener {
            keywordStr = it?.toString().toString()
        }
        val listData = arrayListOf(
            MenuDto(title = "综合", checked = true, value = "0"),
            MenuDto(title = "销量", checked = false, value = "2"),
            MenuDto(title = "价格", checked = false, value = "4"),
            MenuDto(title = "优惠", checked = false, value = "6"),
        )
        listData.forEach {
            goods_sort?.newTab()?.setText(it.title)?.let { it1 -> goods_sort?.addTab(it1) }
        }
        goods_sort?.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                paixu = listData[tab?.position!!].value.toString()
                pageIndex = 1
                searchGoods()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                
            }

        })

        goodsList?.let {
            it.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            homeGoodsListAdapter = SearchGoodsListAdapter(this)
            it.adapter = homeGoodsListAdapter
            it.addItemDecoration(
                GridSpacingItemDecoration(
                    2,
                    ConversionUtils.dp2Px(10f),
                    false
                )
            )
        }

        refresh?.setOnRefreshListener {
            pageIndex = 1
            searchGoods()
        }
        refresh?.setOnLoadMoreListener {
            pageIndex++
            searchGoods()
        }
        is_youquan?.setOnCheckedChangeListener { buttonView, isChecked ->
            isYouquan = if (isChecked) {
                1
            } else {
                0
            }
            pageIndex = 1
            searchGoods()
        }
    }

    override fun initData() {

        searchGoods()
    }

    private fun searchGoods() {
        AppHelper.saveSearchHistory(keywordStr)
        EasyHttp.get(this)
            .api(SearchGoodsApi().apply {
                keyword = keywordStr
                min_id = pageIndex
                is_coupon = isYouquan
                sort = paixu
            })
            .request(object : OnHttpListener<HttpData<ArrayList<GoodsDetailDto>>> {
                override fun onSucceed(result: HttpData<ArrayList<GoodsDetailDto>>?) {
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                    if (pageIndex == 1) {
                        homeGoodsListAdapter?.clearData()
                    }
                    val tempData = arrayListOf<AdDto>()
                    result?.getData()?.forEachIndexed { index, goodsDetailDto ->
                        tempData.add(AdDto(DataType.DATE, goodsDetailDto))
                    }
                    homeGoodsListAdapter?.addData(tempData)
                    loadFeedAd()
                }

                override fun onFail(e: Exception?) {
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                    toast(e?.message)
                }
            })
    }

    @SingleClick
    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.iv_back -> {
                finish()
            }
            R.id.btn_search -> {
                pageIndex = 1
                searchGoods()
            }
            R.id.sort_view -> {
//                showPartShadow(view)
            }
            else -> {}
        }
    }
//
//    private fun showPartShadow(v: View) {
//        if (zonghePopupView == null) {
//            val listData = arrayListOf(
//                MenuDto(title = "综合排序", checked = true, value = "new"),
//                MenuDto(title = "总销量从小到大排序", checked = false, value = "total_sale_num_asc"),
//                MenuDto(title = "总销量从大到小排序", checked = false, value = "total_sale_num_desc"),
//                MenuDto(title = "月销量从小到大排序", checked = false, value = "sale_num_asc"),
//                MenuDto(title = "月销量从大到小排序", checked = false, value = "sale_num_desc"),
//                MenuDto(title = "价格从小到大排序", checked = false, value = "price_asc"),
//                MenuDto(title = "价格从大到小排序", checked = false, value = "price_desc"),
//            )
//            val zongheShadowPopupView = ZongheShadowPopupView(
//                this,
//                listData
//            )
//            zongheShadowPopupView.setListener(object : ZongheShadowPopupView.OnListener<MenuDto> {
//                override fun onSelected(
//                    popupWindow: ZongheShadowPopupView?,
//                    position: Int,
//                    data: MenuDto
//                ) {
//                    paixu = data.value.toString()
//                    pageIndex = 1
//                    searchGoods()
//                }
//            })
//            zonghePopupView = XPopup.Builder(this)
//                .atView(v)
//                .hasStatusBarShadow(false)
//                .asCustom(
//                    zongheShadowPopupView
//                ) as ZongheShadowPopupView
//        }
//        zonghePopupView?.show()
//    }



    private fun loadFeedAd() {
        /** 1、创建AdSlot对象  */
        val adSlot = AdSlot.Builder()
            .setCodeId("102650196")
            .setImageAcceptedSize(
                ScreenUtils.getScreenWidth() / 2,
                ConvertUtils.dp2px(340f)
            ) // 单位px
            .setAdCount(1) // 请求广告数量为1到3条 （优先采用平台配置的数量）
            .build()

        /** 2、创建TTAdNative对象  */
        val adNativeLoader: TTAdNative = TTAdSdk.getAdManager().createAdNative(this)
        /** 3、创建加载、展示监听器  */
        initListeners()
        /** 4、加载广告  */
        adNativeLoader.loadFeedAd(adSlot, mFeedAdListener)
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
                                    homeGoodsListAdapter?.let {
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
    }

    override fun onDestroy() {
        super.onDestroy()
        /** 6、在onDestroy中销毁广告  */
        val mData = homeGoodsListAdapter?.getData()
        if (mData != null) {
            for (itemData in mData) {
                if (itemData.type == DataType.AD) {
                    (itemData.data as TTFeedAd).destroy()
                }
            }
        }
    }
}