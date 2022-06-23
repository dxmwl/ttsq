package com.easybuy.mobile.ui.fragment

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.easybug.mobile.R
import com.easybuy.mobile.app.Constants
import com.easybuy.mobile.app.TitleBarFragment
import com.easybuy.mobile.http.api.HomeGoodsListApi
import com.easybuy.mobile.http.model.HttpData
import com.easybuy.mobile.http.model.MenuDto
import com.easybuy.mobile.ui.activity.HomeActivity
import com.easybuy.mobile.ui.adapter.BannerAdapter
import com.easybuy.mobile.ui.adapter.HomeGoodsListAdapter
import com.easybuy.mobile.ui.adapter.HomeMenuListAdapter
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.youth.banner.Banner

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 首页 Fragment
 */
class HomeFragment : TitleBarFragment<HomeActivity>() {

    private var homeGoodsListAdapter: HomeGoodsListAdapter? = null
    private val banner: Banner<String, BannerAdapter>? by lazy { findViewById(R.id.banner) }
    private val menuList: RecyclerView? by lazy { findViewById(R.id.menu_list) }
    private val goodsList: RecyclerView? by lazy { findViewById(R.id.goods_list) }
    private val refresh: SmartRefreshLayout? by lazy { findViewById(R.id.refresh) }

    companion object {

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.home_fragment
    }

    override fun initView() {

        menuList?.let {
            val arrayListOf = arrayListOf(
                MenuDto(R.mipmap.launcher_ic, "淘宝"),
                MenuDto(R.mipmap.launcher_ic, "天猫"),
                MenuDto(R.mipmap.launcher_ic, "聚划算"),
                MenuDto(R.mipmap.launcher_ic, "抖音"),
                MenuDto(R.mipmap.launcher_ic, "天猫超市"),
                MenuDto(R.mipmap.launcher_ic, "省钱宝"),
                MenuDto(R.mipmap.launcher_ic, "9.9包邮"),
                MenuDto(R.mipmap.launcher_ic, "19.9包邮"),
                MenuDto(R.mipmap.launcher_ic, "热销商品"),
                MenuDto(R.mipmap.launcher_ic, "全部")
            )
            it.layoutManager = GridLayoutManager(context, 5)
            val homeMenuListAdapter = context?.let { it1 -> HomeMenuListAdapter(it1) }
            it.adapter = homeMenuListAdapter
            homeMenuListAdapter?.setData(arrayListOf)
        }

        goodsList?.let {
            it.layoutManager = GridLayoutManager(context, 2)
            homeGoodsListAdapter = context?.let { it1 -> HomeGoodsListAdapter(it1) }
            it.adapter = homeGoodsListAdapter
        }

        refresh?.setOnRefreshListener {
            pageIndex = 1
            getGoodsList()
        }
        refresh?.setOnLoadMoreListener {
            pageIndex++
            getGoodsList()
        }
    }

    override fun initData() {
        getGoodsList()
        banner?.let {
            val arrayListOf = arrayListOf(
                Constants.TEST_IMG_URL,
                Constants.TEST_IMG_URL,
                Constants.TEST_IMG_URL,
                Constants.TEST_IMG_URL,
                Constants.TEST_IMG_URL
            )
            it.setBannerGalleryEffect(39, 16)
            it.addBannerLifecycleObserver(this)
            it.setAdapter(BannerAdapter(arrayListOf))
        }
    }

    private var pageIndex = 1

    /**
     * 获取商品列表
     */
    private fun getGoodsList() {
        EasyHttp.get(this)
            .api(HomeGoodsListApi().apply {
                page = pageIndex
            })
            .request(object : OnHttpListener<HttpData<ArrayList<HomeGoodsListApi.GoodsBean>>> {
                override fun onSucceed(result: HttpData<ArrayList<HomeGoodsListApi.GoodsBean>>?) {
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

    override fun isStatusBarEnabled(): Boolean {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled()
    }
}