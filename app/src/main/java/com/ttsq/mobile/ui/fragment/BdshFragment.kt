package com.ttsq.mobile.ui.fragment

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.blankj.utilcode.util.GsonUtils
import com.google.android.material.tabs.TabLayout
import com.gyf.immersionbar.ImmersionBar
import com.hjq.base.FragmentPagerAdapter
import com.hjq.http.EasyHttp
import com.hjq.http.listener.HttpCallback
import com.hjq.http.listener.OnHttpListener
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.orhanobut.logger.Logger
import com.ttsq.mobile.R
import com.ttsq.mobile.aop.Permissions
import com.ttsq.mobile.aop.SingleClick
import com.ttsq.mobile.app.AppApplication
import com.ttsq.mobile.app.AppFragment
import com.ttsq.mobile.app.TitleBarFragment
import com.ttsq.mobile.http.api.GetBdshBannerApi
import com.ttsq.mobile.http.api.GetDyGoodsClassifyApi
import com.ttsq.mobile.http.api.GetElmListApi
import com.ttsq.mobile.http.api.GetMenuListApi
import com.ttsq.mobile.http.api.SwitchActivityLinElmApi
import com.ttsq.mobile.http.api.SwitchActivityLinMeituanApi
import com.ttsq.mobile.http.model.HttpData
import com.ttsq.mobile.manager.UserManager
import com.ttsq.mobile.other.PermissionInterceptor
import com.ttsq.mobile.ui.activity.BrowserActivity
import com.ttsq.mobile.ui.activity.HomeActivity
import com.ttsq.mobile.ui.adapter.BannerBdshAdapter
import com.ttsq.mobile.ui.adapter.HomeMenuListAdapter
import com.ttsq.mobile.utils.livebus.LiveDataBus
import com.youth.banner.Banner


/**
 * @ClassName: BdshFragment
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/21 0021 14:54
 **/
class BdshFragment : TitleBarFragment<HomeActivity>(), AMapLocationListener {

    companion object {
        fun newInstance(): BdshFragment {
            return BdshFragment()
        }
    }

    private lateinit var homeMenuListAdapter: HomeMenuListAdapter
    private var bannerBdshAdapter: BannerBdshAdapter? = null
    private val banner: Banner<GetBdshBannerApi.TimeActivity, BannerBdshAdapter>? by lazy {
        findViewById(
            R.id.banner
        )
    }
    private val tabLayout: TabLayout? by lazy { findViewById(R.id.tabLayout) }
    private val home_vp: ViewPager? by lazy { findViewById(R.id.vp) }
    private val menuList: RecyclerView? by lazy { findViewById(R.id.menu_list) }
    private val tv_location: TextView? by lazy { findViewById(R.id.tv_location) }
    private var meituanWmDto: ArrayList<GetBdshBannerApi.RedActivity>? = null
    private var elmWmDto: ArrayList<GetBdshBannerApi.RedActivity>? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_bdsh
    }

    override fun initView() {

        setOnClickListener(R.id.btn_meituan, R.id.btn_elm)

        banner?.let {
//            it.setBannerGalleryEffect(39, 16)
            it.addBannerLifecycleObserver(this)
        }
        bannerBdshAdapter = BannerBdshAdapter(ArrayList())
        banner?.setAdapter(bannerBdshAdapter)
        bannerBdshAdapter?.setOnItemClickListener(object : BannerBdshAdapter.OnItemClick {
            override fun clickItem(data: GetBdshBannerApi.TimeActivity?) {
                switchActivityLinkMeituan(data?.activity_id)
            }
        })
        menuList?.let {
            it.layoutManager = GridLayoutManager(context, 5)
            homeMenuListAdapter = HomeMenuListAdapter(requireContext())
            it.adapter = homeMenuListAdapter
        }

        LiveDataBus.subscribe("getLocation", this) { data ->
            requestLocation()
        }
    }

    override fun initData() {
        getBannerList()
        getMenuList(0)
        getElmList()
    }

    private fun getGoodsClassify() {
        EasyHttp.get(this)
            .api(GetDyGoodsClassifyApi())
            .request(object : OnHttpListener<HttpData<GetDyGoodsClassifyApi.GoodsClasifyDto>> {
                override fun onSucceed(result: HttpData<GetDyGoodsClassifyApi.GoodsClasifyDto>?) {
                    result?.getData()?.list?.let { it1 ->
                        tabLayout?.setupWithViewPager(home_vp)
                        val homeFragmentAdapter =
                            FragmentPagerAdapter<AppFragment<*>>(this@BdshFragment)
                        it1.forEach {
                            homeFragmentAdapter.addFragment(
                                DyGoodsFragment.newInstance(it.`val`),
                                it.title
                            )
                        }
                        home_vp?.adapter = homeFragmentAdapter
                    }
                }

                override fun onFail(e: Exception?) {

                }

            })
    }

    private fun getElmList() {
        EasyHttp.get(this)
            .api(GetElmListApi())
            .request(object : OnHttpListener<HttpData<GetBdshBannerApi.BdshBannerDto>> {
                override fun onSucceed(result: HttpData<GetBdshBannerApi.BdshBannerDto>?) {
                    result?.getData()?.let {
                        elmWmDto = it.red_activity
                    }
                }

                override fun onFail(e: Exception?) {
                    toast(e?.message)
                }

            })
    }

    private fun getMenuList(type: Int) {
        EasyHttp.post(this)
            .api(GetMenuListApi().apply {
                this.type = type
            })
            .request(object : OnHttpListener<HttpData<ArrayList<GetMenuListApi.AppMenuDto>>> {
                override fun onSucceed(result: HttpData<ArrayList<GetMenuListApi.AppMenuDto>>?) {
                    result?.getData()?.let {
                        homeMenuListAdapter.setData(it)
                    }
                }

                override fun onFail(e: Exception?) {

                }

            })
    }

    @SingleClick
    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_meituan -> {
                if (meituanWmDto.isNullOrEmpty()) {
                    return
                }
                switchActivityLinkMeituan(meituanWmDto?.get(0)?.activity_id)
            }

            R.id.btn_elm -> {
                if (elmWmDto.isNullOrEmpty()) {
                    return
                }
                switchActivityLinkElm(elmWmDto?.get(0)?.activity_id)
            }

            else -> {}
        }
    }

    private fun getBannerList() {
        EasyHttp.get(this)
            .api(GetBdshBannerApi())
            .request(object : OnHttpListener<HttpData<GetBdshBannerApi.BdshBannerDto>> {
                override fun onSucceed(result: HttpData<GetBdshBannerApi.BdshBannerDto>?) {
                    result?.getData()?.let {
                        meituanWmDto = it.red_activity
                        bannerBdshAdapter?.setDatas(it.time_activity)
                    }
                }

                override fun onFail(e: Exception?) {
                    toast(e?.message)
                }

            })
    }

    /**
     * 美团活动转链
     */
    private fun switchActivityLinkMeituan(activityId: String?) {
        EasyHttp.post(this)
            .api(SwitchActivityLinMeituanApi().apply {
                activity_id = activityId
            })
            .request(object :
                HttpCallback<HttpData<SwitchActivityLinMeituanApi.SwitchActivityLinkDto>>(this) {
                override fun onSucceed(result: HttpData<SwitchActivityLinMeituanApi.SwitchActivityLinkDto>?) {
                    result?.getData()?.let {
                        BrowserActivity.start(requireContext(), it.url)
                    }
                }
            })
    }

    private fun switchActivityLinkElm(activityId: String?) {
        EasyHttp.post(this)
            .api(SwitchActivityLinElmApi().apply {
                activity_id = activityId
                sid = UserManager.userInfo?.userId
            })
            .request(object :
                HttpCallback<HttpData<SwitchActivityLinMeituanApi.SwitchActivityLinkDto>>(this) {
                override fun onSucceed(result: HttpData<SwitchActivityLinMeituanApi.SwitchActivityLinkDto>?) {
                    result?.getData()?.let {
                        BrowserActivity.start(requireContext(), it.h5_short_link)
                    }
                }
            })
    }

    //声明AMapLocationClient类对象
    private var mLocationClient: AMapLocationClient? = null

    private fun requestLocation() {
        XXPermissions.with(this)
            .permission(Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION)
            .interceptor(PermissionInterceptor("请求位置权限，用于搜索附近的优惠信息"))
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                    //初始化定位
                    mLocationClient = AMapLocationClient(AppApplication.getApp())
                    //设置定位回调监听
                    mLocationClient?.setLocationListener(this@BdshFragment)
                    val option = AMapLocationClientOption()
                    //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
                    option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                    /**
                     * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
                     */
                    option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
                    //获取一次定位结果：
                    //该方法默认为false。
                    option.setOnceLocation(true);
                    //设置是否返回地址信息（默认返回地址信息）
                    option.setNeedAddress(true);
                    mLocationClient?.setLocationOption(option);
                    //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
                    mLocationClient?.stopLocation();
                    mLocationClient?.startLocation();
                }
            })
    }

    override fun onLocationChanged(p0: AMapLocation?) {
        if (p0 != null) {
            Logger.d(GsonUtils.toJson(p0))
            tv_location?.text = p0.city
            UserManager.currentLocation = p0
            getGoodsClassify()
        }
    }

    override fun isStatusBarEnabled(): Boolean {
        return false
    }
}