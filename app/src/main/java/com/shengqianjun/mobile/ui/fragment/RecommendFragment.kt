package com.shengqianjun.mobile.ui.fragment

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hjq.base.BaseAdapter
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.shengqianjun.mobile.R
import com.shengqianjun.mobile.app.AppFragment
import com.shengqianjun.mobile.http.api.HomeBannerApi
import com.shengqianjun.mobile.http.api.HomeCainixihuanApi
import com.shengqianjun.mobile.http.api.HomeGoodsListApi
import com.shengqianjun.mobile.http.model.HttpData
import com.shengqianjun.mobile.http.model.MenuDto
import com.shengqianjun.mobile.ui.activity.BrowserActivity
import com.shengqianjun.mobile.ui.activity.HomeActivity
import com.shengqianjun.mobile.ui.activity.ShengqianbaoActivity
import com.shengqianjun.mobile.ui.adapter.BannerAdapter
import com.shengqianjun.mobile.ui.adapter.HomeMenuListAdapter
import com.shengqianjun.mobile.ui.adapter.SearchGoodsListAdapter
import com.umeng.commonsdk.UMConfigure
import com.youth.banner.Banner

class RecommendFragment : AppFragment<HomeActivity>() {

    private val banner: Banner<HomeBannerApi.BannerBean, BannerAdapter>? by lazy { findViewById(R.id.banner) }
    private val menuList: RecyclerView? by lazy { findViewById(R.id.menu_list) }
    private var homeGoodsListAdapter: SearchGoodsListAdapter? = null
    private val refresh: SmartRefreshLayout? by lazy { findViewById(R.id.refresh) }
    private val goodsList: RecyclerView? by lazy { findViewById(R.id.goods_list) }

    companion object {

        fun newInstance(): RecommendFragment {
            return RecommendFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_recommend
    }

    override fun initView() {
        banner?.let {
            it.setBannerGalleryEffect(39, 16)
            it.addBannerLifecycleObserver(this)
        }
        menuList?.let {
            val arrayListOf = arrayListOf(
                MenuDto(
                    id = "1",
                    resId = "https://img-haodanku-com.cdn.fudaiapp.com/0_1624081206_73798",
                    title = "饿了么",
                    value = "https://hdkcmsc22.kuaizhan.com/?cid=Ymg7Vc2#/propaganda?id=116"
                ),
                MenuDto(
                    id = "2",
                    resId = "https://img-haodanku-com.cdn.fudaiapp.com/0_1639987106_638617",
                    title = "抖音好货",
                    value = "https://hdkcmsc22.kuaizhan.com/?cid=Ymg7Vc2#/inside-page/dylist"
                ),
                MenuDto(
                    id = "3",
                    resId = "https://img-haodanku-com.cdn.fudaiapp.com/0_1648537480_628688",
                    title = "百亿补贴",
                    value = "https://hdkcmsc22.kuaizhan.com/?cid=Ymg7Vc2#/propaganda?id=4"
                ),
                MenuDto(
                    id = "4",
                    resId = "https://img-haodanku-com.cdn.fudaiapp.com/0_1624081152_7799",
                    title = "福利线报",
                    value = "https://hdkcmsc22.kuaizhan.com/?cid=Ymg7Vc2&tmp=rt_xb&code=Ymg7Vc2&sp=#/sp"
                ),
                MenuDto(
                    id = "5",
                    resId = "https://img-haodanku-com.cdn.fudaiapp.com/0_1624081228_51540",
                    title = "聚划算",
                    value = "https://hdkcmsc22.kuaizhan.com/?cid=Ymg7Vc2&tmp=juhuasuan&code=Ymg7Vc2&sp=#/sp"
                ),
                MenuDto(
                    id = "6",
                    resId = "https://img-haodanku-com.cdn.fudaiapp.com/0_1624203264_377314",
                    title = "9.9包邮",
                    value = "https://hdkcmsc22.kuaizhan.com/?cid=Ymg7Vc2&tmp=lowprice&code=Ymg7Vc2&sp=#/sp"
                ),
                MenuDto(
                    id = "7",
                    resId = "https://img-haodanku-com.cdn.fudaiapp.com/0_1636017658_700397",
                    title = "生活必需品",
                    value = "https://hdkcmsc22.kuaizhan.com/?cid=Ymg7Vc2&tmp=activity125&code=Ymg7Vc2&sp=#/sp"
                ),
                MenuDto(
                    id = "8",
                    resId = "https://img-haodanku-com.cdn.fudaiapp.com/0_1624081329_908029",
                    title = "热销专场",
                    value = "https://hdkcmsc22.kuaizhan.com/?cid=Ymg7Vc2&tmp=hot_sale&code=Ymg7Vc2&sp=#/sp"
                ),
                MenuDto(id = "9", resId = R.drawable.icon_shengqianbao, title = "省钱宝"),
                MenuDto(
                    id = "10",
                    resId = "https://img-haodanku-com.cdn.fudaiapp.com/0_1624203252_511524",
                    title = "防疫专区",
                    value = "https://hdkcmsc22.kuaizhan.com/?cid=Ymg7Vc2&tmp=fangyi&code=Ymg7Vc2&sp=#/sp"
                )
            )
            it.layoutManager = GridLayoutManager(context, 5)
            val homeMenuListAdapter = context?.let { it1 -> HomeMenuListAdapter(it1) }
            homeMenuListAdapter?.setOnItemClickListener(object : BaseAdapter.OnItemClickListener {
                override fun onItemClick(
                    recyclerView: RecyclerView?,
                    itemView: View?,
                    position: Int
                ) {
                    val menuDto = arrayListOf[position]
                    when (menuDto.id) {
                        "9" -> {
                            startActivity(ShengqianbaoActivity::class.java)
                        }
                        else -> {
                            BrowserActivity.start(requireContext(), menuDto.value.toString())
                        }
                    }
                }

            })
            it.adapter = homeMenuListAdapter
            homeMenuListAdapter?.setData(arrayListOf)
        }

        goodsList?.let {
            it.layoutManager = GridLayoutManager(context, 2)
            homeGoodsListAdapter = context?.let { it1 -> SearchGoodsListAdapter(it1) }
            it.adapter = homeGoodsListAdapter
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
    }

    private var pageIndex = 1

    override fun initData() {
        getBannerList()
        getGoodsList()
    }

    /**
     * 获取轮播图数据
     */
    private fun getBannerList() {
        EasyHttp.get(this)
            .api(HomeBannerApi())
            .request(object : OnHttpListener<HttpData<ArrayList<HomeBannerApi.BannerBean>>> {
                override fun onSucceed(result: HttpData<ArrayList<HomeBannerApi.BannerBean>>?) {
                    banner?.setAdapter(BannerAdapter(result?.getData()))
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
        UMConfigure.getOaid(requireContext()) { oaidStr ->
            if (oaidStr.isNullOrBlank()) {
                //获取不到的话,则获取优选商品
                getYouxuanGoods()
                return@getOaid
            }

            EasyHttp.get(this@RecommendFragment)
                .api(HomeCainixihuanApi().apply {
                    page = pageIndex
                    device_value = oaidStr.toString()
                })
                .request(object :
                    OnHttpListener<HttpData<HomeCainixihuanApi.CainixihuanGoodsInfo>> {
                    override fun onSucceed(result: HttpData<HomeCainixihuanApi.CainixihuanGoodsInfo>?) {
                        refresh?.finishRefresh()
                        refresh?.finishLoadMore()
                        if (pageIndex == 1) {
                            homeGoodsListAdapter?.clearData()
                        }
                        homeGoodsListAdapter?.addData(result?.getData()?.result_list?.map_data)
                    }

                    override fun onFail(e: Exception?) {
                        refresh?.finishRefresh()
                        refresh?.finishLoadMore()
                        toast(e?.message)
                    }
                })
        }
    }

    private fun getYouxuanGoods() {
        EasyHttp.get(this)
            .api(HomeGoodsListApi().apply {
                page = pageIndex
            })
            .request(object :
                OnHttpListener<HttpData<java.util.ArrayList<HomeGoodsListApi.GoodsBean>>> {
                override fun onSucceed(result: HttpData<java.util.ArrayList<HomeGoodsListApi.GoodsBean>>?) {
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                    if (pageIndex == 1) {
                        homeGoodsListAdapter?.clearData()
                    }
                    homeGoodsListAdapter?.addData(result?.getData())
                }

                override fun onFail(e: Exception?) {
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                    toast(e?.message)
                }
            })
    }
}