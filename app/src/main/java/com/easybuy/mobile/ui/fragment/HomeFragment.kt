package com.easybuy.mobile.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.PhoneUtils
import com.easybug.mobile.R
import com.easybuy.mobile.aop.SingleClick
import com.easybuy.mobile.app.TitleBarFragment
import com.easybuy.mobile.http.api.*
import com.easybuy.mobile.http.model.HttpData
import com.easybuy.mobile.http.model.MenuDto
import com.easybuy.mobile.other.AppConfig
import com.easybuy.mobile.ui.activity.BrowserActivity
import com.easybuy.mobile.ui.activity.HomeActivity
import com.easybuy.mobile.ui.activity.SearchActivity
import com.easybuy.mobile.ui.activity.ShengqianbaoActivity
import com.easybuy.mobile.ui.adapter.BannerAdapter
import com.easybuy.mobile.ui.adapter.HomeGoodsListAdapter
import com.easybuy.mobile.ui.adapter.HomeMenuListAdapter
import com.easybuy.mobile.ui.adapter.SearchGoodsListAdapter
import com.easybuy.mobile.ui.dialog.MessageDialog
import com.hjq.base.BaseAdapter
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.hjq.shape.view.ShapeTextView
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.umeng.commonsdk.UMConfigure
import com.umeng.commonsdk.listener.OnGetOaidListener
import com.youth.banner.Banner

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 首页 Fragment
 */
class HomeFragment : TitleBarFragment<HomeActivity>() {


    private val banner: Banner<HomeBannerApi.BannerBean, BannerAdapter>? by lazy { findViewById(R.id.banner) }
    private val menuList: RecyclerView? by lazy { findViewById(R.id.menu_list) }
    private var homeGoodsListAdapter: SearchGoodsListAdapter? = null
    private val refresh: SmartRefreshLayout? by lazy { findViewById(R.id.refresh) }
    private val search_view: ShapeTextView? by lazy { findViewById(R.id.search_view) }
    private val shengqianbao: ImageView? by lazy { findViewById(R.id.shengqianbao) }
    private val goodsList: RecyclerView? by lazy { findViewById(R.id.goods_list) }

    companion object {

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.home_fragment
    }

    override fun initView() {
        setOnClickListener(search_view, shengqianbao)
        banner?.let {
            it.setBannerGalleryEffect(39, 16)
            it.addBannerLifecycleObserver(this)
        }
        menuList?.let {
            val arrayListOf = arrayListOf(
                MenuDto(id = "1", resId = R.mipmap.ic_launcher, title = "淘宝"),
                MenuDto(id = "2", resId = R.mipmap.ic_launcher, title = "天猫"),
                MenuDto(id = "3", resId = R.mipmap.ic_launcher, title = "聚划算"),
                MenuDto(id = "4", resId = R.mipmap.ic_launcher, title = "抖音"),
                MenuDto(id = "5", resId = R.mipmap.ic_launcher, title = "天猫超市"),
                MenuDto(id = "6", resId = R.mipmap.ic_launcher, title = "省钱宝"),
                MenuDto(id = "7", resId = R.mipmap.ic_launcher, title = "9.9包邮"),
                MenuDto(id = "8", resId = R.mipmap.ic_launcher, title = "19.9包邮"),
                MenuDto(id = "9", resId = R.mipmap.ic_launcher, title = "热销商品"),
                MenuDto(id = "-1", resId = R.mipmap.ic_launcher, title = "全部")
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
                        "6" -> {
                            startActivity(ShengqianbaoActivity::class.java)
                        }
                        "7" -> {
                            BrowserActivity.start(requireContext(), AppConfig.getJiukuaijiuUrl())
                        }
                        else -> {}
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

    @SingleClick
    override fun onClick(view: View) {
        when (view) {
            search_view -> {
                startActivity(SearchActivity::class.java)
            }
            shengqianbao -> {
                startActivity(ShengqianbaoActivity::class.java)
            }
            else -> {}
        }
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
            if (oaidStr.isNullOrBlank()){
                //获取不到的话,则获取优选商品
                getYouxuanGoods()
                return@getOaid
            }

            EasyHttp.get(this@HomeFragment)
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
            .request(object : OnHttpListener<HttpData<java.util.ArrayList<HomeGoodsListApi.GoodsBean>>> {
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

    override fun isStatusBarEnabled(): Boolean {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled()
    }
}