package com.ttsq.mobile.ui.activity

import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.hjq.base.FragmentPagerAdapter
import com.ttsq.mobile.R
import com.ttsq.mobile.app.AppActivity
import com.ttsq.mobile.app.AppFragment
import com.ttsq.mobile.ui.fragment.BangdanVpFragment
import com.ttsq.mobile.ui.fragment.OrderVpFragment

/**
 * @ClassName: MyOrderActivity
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/04 0004 13:23
 **/
class MyOrderActivity: AppActivity() {

    private val tablayout: TabLayout? by lazy { findViewById(R.id.tablayout) }
    private val order_vp: ViewPager? by lazy { findViewById(R.id.order_vp) }

    override fun getLayoutId(): Int {
        return R.layout.activity_my_order
    }

    override fun initView() {
        val pageAdapter = FragmentPagerAdapter<AppFragment<*>>(this)
        pageAdapter.addFragment(OrderVpFragment.newInstance(12),"已付款")
        pageAdapter.addFragment(OrderVpFragment.newInstance(14),"已收货")
        pageAdapter.addFragment(OrderVpFragment.newInstance(3),"已结算")
        pageAdapter.addFragment(OrderVpFragment.newInstance(13),"已失效")
        order_vp?.adapter = pageAdapter
        tablayout?.setupWithViewPager(order_vp)
    }

    override fun initData() {
        
    }
}