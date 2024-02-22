package com.ttsq.mobile.ui.fragment

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ScreenUtils
import com.bumptech.glide.Glide
import com.bytedance.sdk.openadsdk.AdSlot
import com.bytedance.sdk.openadsdk.TTAdDislike
import com.bytedance.sdk.openadsdk.TTAdNative
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.bytedance.sdk.openadsdk.TTFeedAd
import com.bytedance.sdk.openadsdk.mediation.ad.MediationExpressRenderListener
import com.hjq.base.BaseAdapter
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.orhanobut.logger.Logger
import com.pdlbox.tools.utils.ConversionUtils
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.ttsq.mobile.R
import com.ttsq.mobile.app.AppFragment
import com.ttsq.mobile.app.Constants
import com.ttsq.mobile.http.api.CheckUserFreeGoodsApi
import com.ttsq.mobile.http.api.GetFreeGoodsListApi
import com.ttsq.mobile.http.api.GetMenuListApi
import com.ttsq.mobile.http.api.HomeBannerApi
import com.ttsq.mobile.http.api.HomeGoodsListApi
import com.ttsq.mobile.http.api.RecommendPinpaiApi
import com.ttsq.mobile.http.model.AdDto
import com.ttsq.mobile.http.model.DataType
import com.ttsq.mobile.http.model.HttpData
import com.ttsq.mobile.http.model.MenuDto
import com.ttsq.mobile.other.AppConfig
import com.ttsq.mobile.other.GridSpacingItemDecoration
import com.ttsq.mobile.ui.activity.*
import com.ttsq.mobile.ui.adapter.BannerAdapter
import com.ttsq.mobile.ui.adapter.HomeFreeGoodsListAdapter
import com.ttsq.mobile.ui.adapter.HomeMenuListAdapter
import com.ttsq.mobile.ui.adapter.PinpaiGoodsAdapter
import com.ttsq.mobile.ui.adapter.SearchGoodsListAdapter
import com.ttsq.mobile.ui.dialog.FreeGoodsDialog
import com.youth.banner.Banner


class RecommendFragment : AppFragment<HomeActivity>() {

    private lateinit var homeMenuListAdapter: HomeMenuListAdapter
    private lateinit var homFreeGoodsListAdapter: HomeFreeGoodsListAdapter
    private lateinit var pinpaiGoodsAdapter: PinpaiGoodsAdapter
    private val banner: Banner<HomeBannerApi.BannerBean, BannerAdapter>? by lazy { findViewById(R.id.banner) }
    private val menuList: RecyclerView? by lazy { findViewById(R.id.menu_list) }
    private var homeGoodsListAdapter: SearchGoodsListAdapter? = null
    private val refresh: SmartRefreshLayout? by lazy { findViewById(R.id.refresh) }
    private val goodsList: RecyclerView? by lazy { findViewById(R.id.goods_list) }
    private val pinpai_banner: ImageView? by lazy { findViewById(R.id.pinpai_banner) }
    private val logo_img: ImageView? by lazy { findViewById(R.id.logo_img) }
    private val textView11: TextView? by lazy { findViewById(R.id.textView11) }
    private val pinpai_biaoyu: TextView? by lazy { findViewById(R.id.pinpai_biaoyu) }
    private val pinpai_goods_list: RecyclerView? by lazy { findViewById(R.id.pinpai_goods_list) }
    private val free_goods_list: RecyclerView? by lazy { findViewById(R.id.free_goods_list) }
    private val layout_free_goods: ConstraintLayout? by lazy { findViewById(R.id.layout_free_goods) }

    private var mFeedAdListener: TTAdNative.FeedAdListener? = null // 广告加载监听器

    companion object {

        fun newInstance(): RecommendFragment {
            return RecommendFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_recommend
    }

    override fun initView() {
        setOnClickListener(R.id.layout_pinpai,R.id.layout_free_goods)
        banner?.let {
//            it.setBannerGalleryEffect(39, 16)
            it.addBannerLifecycleObserver(this)
        }
        menuList?.let {
            it.layoutManager = GridLayoutManager(context, 5)
            homeMenuListAdapter = HomeMenuListAdapter(requireContext())
            it.adapter = homeMenuListAdapter
        }

        goodsList?.let {
            it.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            homeGoodsListAdapter = context?.let { it1 -> SearchGoodsListAdapter(it1) }
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
            getBannerList()
            pageIndex = 1
            getGoodsList()
        }
        refresh?.setOnLoadMoreListener {
            pageIndex++
            getGoodsList()
        }
        pinpai_goods_list?.let {
            it.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            pinpaiGoodsAdapter = PinpaiGoodsAdapter(requireContext())
            pinpaiGoodsAdapter.setOnItemClickListener(object : BaseAdapter.OnItemClickListener {
                override fun onItemClick(
                    recyclerView: RecyclerView?,
                    itemView: View?,
                    position: Int,
                ) {
                    val item = pinpaiGoodsAdapter.getItem(position)
                    GoodsDetailActivity.start(requireContext(), item.itemid)
                }

            })
            it.adapter = pinpaiGoodsAdapter
        }

        free_goods_list?.also {
            it.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            homFreeGoodsListAdapter = HomeFreeGoodsListAdapter(requireContext())
            it.adapter = homFreeGoodsListAdapter
        }
    }

    private var pageIndex = 1

    override fun initData() {
        getBannerList()
        getPinpaiList()
        getGoodsList()
        getFreeGoodsList()
        checkUserFreeGoods()
        getMenuList(1)
    }

    private fun getMenuList(type: Int) {
        EasyHttp.post(this)
            .api(GetMenuListApi().apply {
                this.type = type
            })
            .request(object :OnHttpListener<HttpData<ArrayList<GetMenuListApi.AppMenuDto>>>{
                override fun onSucceed(result: HttpData<ArrayList<GetMenuListApi.AppMenuDto>>?) {
                    result?.getData()?.let {
                        homeMenuListAdapter.setData(it)
                    }
                }

                override fun onFail(e: java.lang.Exception?) {

                }

            })
    }


    private fun checkUserFreeGoods() {
        EasyHttp.post(this)
            .api(CheckUserFreeGoodsApi())
            .request(object :
                OnHttpListener<HttpData<CheckUserFreeGoodsApi.CheckUserFreeGoodsDto>> {
                override fun onSucceed(result: HttpData<CheckUserFreeGoodsApi.CheckUserFreeGoodsDto>?) {
                    result?.getData()?.let {
                        if (it.hasFreeGoods){
                            layout_free_goods?.visibility = View.VISIBLE

                            FreeGoodsDialog.Builder(requireContext())
                                .show()
                        }else{
                            //测试用
                            if (AppConfig.isDebug()) {
                                layout_free_goods?.visibility = View.VISIBLE
                            }else{
                                layout_free_goods?.visibility = View.GONE
                            }
                        }
                    }
                }

                override fun onFail(e: java.lang.Exception?) {
                    toast(e?.message)
                }
            })
    }

    private fun getFreeGoodsList() {
        EasyHttp.post(this)
            .api(GetFreeGoodsListApi().apply {

            })
            .request(object :
                OnHttpListener<HttpData<ArrayList<GetFreeGoodsListApi.FreeGoodsDeo>>> {
                override fun onSucceed(result: HttpData<ArrayList<GetFreeGoodsListApi.FreeGoodsDeo>>?) {
                    result?.getData()?.let {
                        homFreeGoodsListAdapter.addData(it)
                    }
                }

                override fun onFail(e: java.lang.Exception?) {
                    toast(e?.message)
                }

            })
    }


    private fun getPinpaiList() {
        EasyHttp.get(this)
            .api(RecommendPinpaiApi())
            .request(object : OnHttpListener<HttpData<RecommendPinpaiApi.RecommendPinpaiDto>> {
                override fun onSucceed(result: HttpData<RecommendPinpaiApi.RecommendPinpaiDto>?) {
                    result?.getData()?.let {
                        pinpai_banner?.let { it1 ->
                            Glide.with(this@RecommendFragment).load(it.data.background).into(it1)
                        }
                        logo_img?.let { it1 ->
                            Glide.with(this@RecommendFragment).load(it.data.brand_logo).into(it1)
                        }
                        textView11?.text = it.data.name
                        pinpai_biaoyu?.text = it.data.title
                        pinpaiGoodsAdapter.setData(it.items)

                    }
                }

                override fun onFail(e: java.lang.Exception?) {
                    toast(e?.message)
                }
            })
    }

    /**
     * 获取轮播图数据
     */
    private fun getBannerList() {
        EasyHttp.get(this)
            .api(HomeBannerApi())
            .request(object : OnHttpListener<HttpData<ArrayList<HomeBannerApi.BannerBean>>> {
                override fun onSucceed(result: HttpData<ArrayList<HomeBannerApi.BannerBean>>?) {
                    result?.getData()?.let {
                        val bannerListData = ArrayList<HomeBannerApi.BannerBean>()
                        it.forEach { bannerItem ->
                            if (bannerItem.image.isNotBlank()) {
                                bannerListData.add(bannerItem)
                            }
                        }
                        banner?.setAdapter(BannerAdapter(bannerListData))
                    }
                }

                override fun onFail(e: java.lang.Exception?) {
                    toast(e?.message)
                }
            })
    }

    /**
     * 获取商品列表
     */
    private fun getGoodsList() {
        getYouxuanGoods()
//        UMConfigure.getOaid(requireContext()) { oaidStr ->
//            if (oaidStr.isNullOrBlank()) {
//                //获取不到的话,则获取优选商品
//                getYouxuanGoods()
//                return@getOaid
//            }
//
//            EasyHttp.get(this@RecommendFragment)
//                .api(HomeCainixihuanApi().apply {
//                    page = pageIndex
//                    device_value = oaidStr.toString()
//                })
//                .request(object :
//                    OnHttpListener<HttpData<HomeCainixihuanApi.CainixihuanGoodsInfo>> {
//                    override fun onSucceed(result: HttpData<HomeCainixihuanApi.CainixihuanGoodsInfo>?) {
//                        refresh?.finishRefresh()
//                        refresh?.finishLoadMore()
//                        if (pageIndex == 1) {
//                            homeGoodsListAdapter?.clearData()
//                        }
////                        homeGoodsListAdapter?.addData(result?.getData()?.result_list?.map_data)
//                    }
//
//                    override fun onFail(e: Exception?) {
//                        refresh?.finishRefresh()
//                        refresh?.finishLoadMore()
//                        toast(e?.message)
//                    }
//                })
//        }
    }

    private fun getYouxuanGoods() {
        EasyHttp.get(this)
            .api(HomeGoodsListApi().apply {
                p = pageIndex
            })
            .request(object :
                OnHttpListener<HttpData<HomeGoodsListApi.MaochaoGoods>> {
                override fun onSucceed(result: HttpData<HomeGoodsListApi.MaochaoGoods>?) {
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                    if (pageIndex == 1) {
                        homeGoodsListAdapter?.clearData()
                    }
                    val tempData = arrayListOf<AdDto>()
                    result?.getData()?.data?.forEachIndexed { index, goodsDetailDto ->
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

    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.layout_pinpai -> {
                startActivity(PinpaiGoodsActivity::class.java)
            }
            R.id.layout_free_goods->{
                startActivity(FreeGoodsActivity::class.java)
            }

            else -> {}
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
                        ad.setDislikeCallback(requireActivity(),
                            object : TTAdDislike.DislikeInteractionCallback {
                                override fun onShow() {

                                }

                                override fun onSelected(p0: Int, p1: String?, p2: Boolean) {
                                    // 用户点击dislike后回调
                                    Logger.d("onSelected: $p0, $p1, $p2")
                                    homeGoodsListAdapter?.getData()?.forEach {
                                        if (it.type == DataType.AD && it.data == ad) {
                                            homeGoodsListAdapter?.removeItem(it)
                                            homeGoodsListAdapter?.notifyDataSetChanged()
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