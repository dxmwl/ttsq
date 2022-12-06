package com.shengqianjun.mobile.ui.activity

import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.hjq.base.FragmentPagerAdapter
import com.shengqianjun.mobile.R
import com.shengqianjun.mobile.app.AppActivity
import com.shengqianjun.mobile.app.AppFragment
import com.shengqianjun.mobile.app.AppHelper
import com.shengqianjun.mobile.ui.fragment.HomeClassGoodsFragment
import com.shengqianjun.mobile.ui.fragment.PinpaiClassGoodsFragment
import com.shengqianjun.mobile.ui.fragment.PinpaiRecommendFragment
import com.shengqianjun.mobile.ui.fragment.RecommendFragment

/**
 * 品牌商品
 */
class PinpaiGoodsActivity: AppActivity() {

    private val pinpai_tab: TabLayout? by lazy { findViewById(R.id.pinpai_tab) }
    private val pinpai_vp: ViewPager? by lazy { findViewById(R.id.pinpai_vp) }

    override fun getLayoutId(): Int {
        return R.layout.activity_pinpai_goods
    }

    override fun initView() {
        pinpai_tab?.setupWithViewPager(pinpai_vp)
        val homeFragmentAdapter = FragmentPagerAdapter<AppFragment<*>>(this)
        homeFragmentAdapter.addFragment(PinpaiRecommendFragment.newInstance(), "推荐")
        AppHelper.bandClassList.forEach {
            homeFragmentAdapter.addFragment(
                PinpaiClassGoodsFragment.newInstance(it.data, it.cid),
                it.main_name
            )
        }
        pinpai_vp?.adapter = homeFragmentAdapter
    }

    override fun initData() {
        
    }
}