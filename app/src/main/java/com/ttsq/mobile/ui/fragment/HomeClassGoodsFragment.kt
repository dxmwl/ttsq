package com.ttsq.mobile.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ScreenUtils
import com.bytedance.sdk.openadsdk.AdSlot
import com.bytedance.sdk.openadsdk.TTAdNative
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.bytedance.sdk.openadsdk.TTFeedAd
import com.bytedance.sdk.openadsdk.mediation.ad.MediationExpressRenderListener
import com.google.android.material.tabs.TabLayout
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.orhanobut.logger.Logger
import com.pdlbox.tools.utils.ConversionUtils
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.ttsq.mobile.R
import com.ttsq.mobile.app.AppFragment
import com.ttsq.mobile.http.api.ClassApi
import com.ttsq.mobile.http.api.CommodityScreeningApi
import com.ttsq.mobile.http.model.AdDto
import com.ttsq.mobile.http.model.DataType
import com.ttsq.mobile.http.model.HttpData
import com.ttsq.mobile.http.model.MenuDto
import com.ttsq.mobile.other.GridSpacingItemDecoration
import com.ttsq.mobile.ui.activity.HomeActivity
import com.ttsq.mobile.ui.activity.SearchResultActivity
import com.ttsq.mobile.ui.adapter.HdkTwoClassAdapter
import com.ttsq.mobile.ui.adapter.SearchGoodsListAdapter
import com.ttsq.mobile.ui.adapter.ShaixuanGoodsListAdapter
import java.lang.Exception


class HomeClassGoodsFragment : AppFragment<HomeActivity>(), HdkTwoClassAdapter.OnItemClickListener {

    private var cidStr: String = ""
    private var childData: ArrayList<ClassApi.Data> = ArrayList()

    companion object {

        val DATAFLAG = "DATAFLAG"
        val CIDFLAG = "cid"

        fun newInstance(data: ArrayList<ClassApi.Data>,cid:String): HomeClassGoodsFragment {
            val homeClassGoodsFragment = HomeClassGoodsFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList(DATAFLAG, data)
            bundle.putString(CIDFLAG, cid)
            homeClassGoodsFragment.arguments = bundle
            return homeClassGoodsFragment
        }
    }

    private var homeGoodsListAdapter: ShaixuanGoodsListAdapter? = null
    private lateinit var threeClassAdapter: HdkTwoClassAdapter.ThreeClassAdapter
    private val goods_class: RecyclerView? by lazy { findViewById(R.id.goods_class) }
    private val goods_sort: TabLayout? by lazy { findViewById(R.id.goods_sort) }
    private val goodsList: RecyclerView? by lazy { findViewById(R.id.goods_list) }
    private val refresh: SmartRefreshLayout? by lazy { findViewById(R.id.refresh) }
    private var isYouquan: Int = 1
    private var paixu: String = "new"
    private var pageIndex: Int = 0
    private var mFeedAdListener: TTAdNative.FeedAdListener? = null // 广告加载监听器

    override fun getLayoutId(): Int {
        return R.layout.fragment_home_goods
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val bundle = arguments
        if (null != bundle) {
            cidStr = bundle.getString(CIDFLAG).toString()
            childData =
                bundle.getParcelableArrayList<ClassApi.Data>(DATAFLAG) as ArrayList<ClassApi.Data>
        }

    }

    override fun initView() {
        goods_class?.let {
            it.layoutManager = GridLayoutManager(requireContext(), 5)
            threeClassAdapter = HdkTwoClassAdapter.ThreeClassAdapter(requireContext(), this)
            it.adapter = threeClassAdapter
        }

        val listData = arrayListOf(
            MenuDto(title = "综合", checked = true, value = "0"),
            MenuDto(title = "销量", checked = false, value = "4"),
            MenuDto(title = "价格", checked = false, value = "1"),
            MenuDto(title = "优惠", checked = false, value = "13"),
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
            it.layoutManager = LinearLayoutManager(requireContext())
            homeGoodsListAdapter = ShaixuanGoodsListAdapter(requireContext())
            it.adapter = homeGoodsListAdapter
        }

        refresh?.setOnRefreshListener {
            pageIndex = 1
            searchGoods()
        }
        refresh?.setOnLoadMoreListener {
            pageIndex++
            searchGoods()
        }
    }

    override fun initData() {
        val classList = ArrayList<ClassApi.Info>()
        childData.forEach {
            classList.addAll(it.info)
        }
        val tempList = if (classList.size > 10) {
            classList.subList(0, 10)
        }else{
            classList
        }
        threeClassAdapter.setData(tempList)
        searchGoods()
    }

    override fun onItemClick(classDataBean: ClassApi.Info) {
        val intent = Intent(getAttachActivity(), SearchResultActivity::class.java)
        intent.putExtra("KEYWORD", classDataBean.son_name)
        startActivity(intent)
    }

    private fun searchGoods() {
        EasyHttp.get(this)
            .api(CommodityScreeningApi().apply {
                cid = cidStr
                min_id = pageIndex
                sort = paixu
            })
            .request(object :OnHttpListener<HttpData<ArrayList<CommodityScreeningApi.ShaixuanGoodsDto>>>{
                override fun onSucceed(result: HttpData<ArrayList<CommodityScreeningApi.ShaixuanGoodsDto>>?) {
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
                }

            })
    }

    private fun loadFeedAd() {
        /** 1、创建AdSlot对象  */
        val adSlot = AdSlot.Builder()
            .setCodeId("102650196")
            .setImageAcceptedSize(
                ScreenUtils.getScreenWidth(),
                ConvertUtils.dp2px(100f)
            ) // 单位px
            .setAdCount(1) // 请求广告数量为1到3条 （优先采用平台配置的数量）
            .build()

        /** 2、创建TTAdNative对象  */
        val adNativeLoader: TTAdNative = TTAdSdk.getAdManager().createAdNative(requireContext())
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