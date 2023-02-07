package com.ttsq.mobile.ui.fragment

import android.view.View
import android.widget.ImageView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.hjq.base.FragmentPagerAdapter
import com.hjq.shape.layout.ShapeRelativeLayout
import com.ttsq.mobile.R
import com.ttsq.mobile.aop.SingleClick
import com.ttsq.mobile.app.AppFragment
import com.ttsq.mobile.app.AppHelper
import com.ttsq.mobile.app.Constants
import com.ttsq.mobile.app.TitleBarFragment
import com.ttsq.mobile.eventbus.RefreshClass
import com.ttsq.mobile.ui.activity.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

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
    private val all_class: ImageView? by lazy { findViewById(R.id.all_class) }
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
        val eventBus = EventBus.getDefault()
        if (eventBus.isRegistered(this).not()) {
            eventBus.register(this)
        }

        setOnClickListener(search_view, shengqianbao, btn_shengqian, all_class)

        btn_shengqian?.let { Glide.with(this).load(R.drawable.shengqiangonglue_img).into(it) }
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
                    "${Constants.URL_CMS}?cid=bBNmDymI#/save-money"
                )
            }
            all_class -> {
                startActivity(AllClassActivity::class.java)
            }
            else -> {}
        }
    }

    override fun isStatusBarEnabled(): Boolean {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshClass(refreshClass: RefreshClass){
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
    }

    override fun onDestroy() {
        super.onDestroy()
        val eventBus = EventBus.getDefault()
        if (eventBus.isRegistered(this)) {
            eventBus.unregister(this)
        }
    }
}