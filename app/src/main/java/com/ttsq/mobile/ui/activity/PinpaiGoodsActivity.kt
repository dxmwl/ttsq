package com.ttsq.mobile.ui.activity

import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.hjq.base.FragmentPagerAdapter
import com.ttsq.mobile.R
import com.ttsq.mobile.app.AppActivity
import com.ttsq.mobile.app.AppFragment
import com.ttsq.mobile.app.AppHelper
import com.ttsq.mobile.ui.fragment.HomeClassGoodsFragment
import com.ttsq.mobile.ui.fragment.PinpaiClassGoodsFragment
import com.ttsq.mobile.ui.fragment.PinpaiRecommendFragment
import com.ttsq.mobile.ui.fragment.RecommendFragment

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