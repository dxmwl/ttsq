package com.shengqianjun.mobile.ui.fragment

import android.view.View
import android.widget.ImageView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.hjq.base.FragmentPagerAdapter
import com.hjq.shape.layout.ShapeRelativeLayout
import com.shengqianjun.mobile.R
import com.shengqianjun.mobile.aop.SingleClick
import com.shengqianjun.mobile.app.AppFragment
import com.shengqianjun.mobile.app.AppHelper
import com.shengqianjun.mobile.app.TitleBarFragment
import com.shengqianjun.mobile.ui.activity.BrowserActivity
import com.shengqianjun.mobile.ui.activity.HomeActivity
import com.shengqianjun.mobile.ui.activity.SearchActivity
import com.shengqianjun.mobile.ui.activity.ShengqianbaoActivity

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 首页 Fragment
 */
class HomeFragment : TitleBarFragment<HomeActivity>() {


    private val search_view: ShapeRelativeLayout? by lazy { findViewById(R.id.search_view) }
    private val shengqianbao: ImageView? by lazy { findViewById(R.id.shengqianbao) }
    private val btn_shengqian: ImageView? by lazy { findViewById(R.id.btn_shengqian) }
    private val home_tab: TabLayout? by lazy { findViewById(R.id.home_tab) }
    private val home_vp: ViewPager? by lazy { findViewById(R.id.home_vp) }

    companion object {

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.home_fragment
    }

    override fun initView() {
        setOnClickListener(search_view, shengqianbao, btn_shengqian)

        btn_shengqian?.let { Glide.with(this).load(R.drawable.shengqiangonglue_img).into(it) }

        postDelayed(Runnable {
            home_tab?.setupWithViewPager(home_vp)
            val homeFragmentAdapter = FragmentPagerAdapter<AppFragment<*>>(this)
            homeFragmentAdapter.addFragment(RecommendFragment.newInstance(), "推荐")
            AppHelper.classData.forEach {
                homeFragmentAdapter.addFragment(
                    HomeClassGoodsFragment.newInstance(it.data, it.cid),
                    it.main_name
                )
            }
            home_vp?.adapter = homeFragmentAdapter
        }, 1000)
    }

    override fun initData() {

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
            btn_shengqian -> {
                BrowserActivity.start(
                    requireContext(),
                    "https://hdkcmsc22.kuaizhan.com/?cid=Ymg7Vc2#/save-money"
                )
            }
            else -> {}
        }
    }

    override fun isStatusBarEnabled(): Boolean {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled()
    }
}