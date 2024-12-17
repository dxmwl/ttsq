package com.ttsq.mobile.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ScreenUtils
import com.bytedance.sdk.openadsdk.AdSlot
import com.bytedance.sdk.openadsdk.TTAdDislike
import com.bytedance.sdk.openadsdk.TTAdNative
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.bytedance.sdk.openadsdk.TTFeedAd
import com.bytedance.sdk.openadsdk.mediation.ad.MediationExpressRenderListener
import com.google.android.material.tabs.TabLayout
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.hjq.shape.view.ShapeCheckBox
import com.orhanobut.logger.Logger
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.ttsq.mobile.R
import com.ttsq.mobile.app.AppFragment
import com.ttsq.mobile.app.AppHelper
import com.ttsq.mobile.http.api.SearchGoodsApi
import com.ttsq.mobile.http.model.AdDto
import com.ttsq.mobile.http.model.DataType
import com.ttsq.mobile.http.model.GoodsDetailDto
import com.ttsq.mobile.http.model.HttpData
import com.ttsq.mobile.http.model.MenuDto
import com.ttsq.mobile.other.GridSpacingItemDecoration
import com.ttsq.mobile.ui.activity.SearchResultActivity
import com.ttsq.mobile.ui.adapter.SearchGoodsListAdapter


class TaobaoSearchResultFragment: AppFragment<SearchResultActivity>() {

    companion object {

        private const val KEY_WORDS = "KEY_WORDS"

        fun newInstance(keywordStr:String): TaobaoSearchResultFragment {
            val taobaoSearchResultFragment = TaobaoSearchResultFragment()
            val bundle = Bundle()
            bundle.putString(KEY_WORDS, keywordStr)
            taobaoSearchResultFragment.arguments = bundle
            return taobaoSearchResultFragment
        }
    }

    private val sort_view: TextView? by lazy { findViewById(R.id.sort_view) }
    private val is_youquan: ShapeCheckBox? by lazy { findViewById(R.id.is_youquan) }
    private val goods_sort: TabLayout? by lazy { findViewById(R.id.goods_sort) }
    private var isYouquan: Int = 1
    private var paixu: String = "0"
    private var mFeedAdListener: TTAdNative.FeedAdListener? = null // 广告加载监听器
    private var pageIndex: Int = 1
    private var keywordStr = ""

    private var homeGoodsListAdapter: SearchGoodsListAdapter? = null
    private val goodsList: RecyclerView? by lazy { findViewById(R.id.goods_list) }
    private val refresh: SmartRefreshLayout? by lazy { findViewById(R.id.refresh) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_taobao_search_result
    }

    override fun initView() {
        keywordStr = getString(KEY_WORDS).toString()

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
            it.layoutManager = GridLayoutManager(requireContext(),2)
            homeGoodsListAdapter = SearchGoodsListAdapter(requireContext())
            it.adapter = homeGoodsListAdapter
            it.addItemDecoration(
                GridSpacingItemDecoration(
                    2,
                    ConvertUtils.dp2px(10f),
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

    private fun loadFeedAd() {
        /** 1、创建AdSlot对象  */
        val adSlot = AdSlot.Builder()
            .setCodeId("102970150")
            .setExpressViewAcceptedSize(
                ConvertUtils.px2dp((ScreenUtils.getScreenWidth() / 2).toFloat()).toFloat(),
                0f
            )
            .setAdCount(1) // 请求广告数量为1到3条 （优先采用平台配置的数量）
            .build()

        /** 2、创建TTAdNative对象  */
        val adNativeLoader: TTAdNative = TTAdSdk.getAdManager().createAdNative(requireActivity())
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
                        ad.setDislikeCallback(requireActivity(),
                            object : TTAdDislike.DislikeInteractionCallback {
                                override fun onShow() {

                                }

                                override fun onSelected(p0: Int, p1: String?, p2: Boolean) {
                                    // 用户点击dislike后回调
                                    Logger.d("onSelected: $p0, $p1, $p2")
                                    homeGoodsListAdapter?.getData()?.forEach {
                                        if (it.type == DataType.AD && it.data == ad) {
//                                            homeGoodsListAdapter?.removeItem(it)
//                                            homeGoodsListAdapter?.notifyDataSetChanged()
                                        }
                                    }
                                }

                                override fun onCancel() {

                                }

                            })
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