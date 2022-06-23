package com.easybuy.mobile.ui.fragment

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.easybug.mobile.R
import com.easybuy.mobile.app.Constants
import com.easybuy.mobile.app.TitleBarFragment
import com.easybuy.mobile.http.model.MenuDto
import com.easybuy.mobile.ui.activity.HomeActivity
import com.easybuy.mobile.ui.adapter.BannerAdapter
import com.easybuy.mobile.ui.adapter.HomeMenuListAdapter
import com.youth.banner.Banner

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 首页 Fragment
 */
class HomeFragment : TitleBarFragment<HomeActivity>() {

    private val banner: Banner<String, BannerAdapter>? by lazy { findViewById(R.id.banner) }
    private val menuList: RecyclerView? by lazy { findViewById(R.id.menu_list) }

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
    }

    override fun initData() {
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

    override fun isStatusBarEnabled(): Boolean {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled()
    }
}