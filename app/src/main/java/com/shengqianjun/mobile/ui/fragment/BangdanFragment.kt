package com.shengqianjun.mobile.ui.fragment

import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.hjq.base.FragmentPagerAdapter
import com.shengqianjun.mobile.R
import com.shengqianjun.mobile.app.AppFragment
import com.shengqianjun.mobile.app.TitleBarFragment
import com.shengqianjun.mobile.ui.activity.HomeActivity

/**
 * 榜单
 */
class BangdanFragment : TitleBarFragment<HomeActivity>() {

    companion object {


        fun newInstance(): BangdanFragment {
            return BangdanFragment()
        }
    }

    private val bangdan_tablayout: TabLayout? by lazy { findViewById(R.id.bangdan_tablayout) }
    private val bangdan_vp: ViewPager? by lazy { findViewById(R.id.bangdan_vp) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_bangdan
    }

    override fun initView() {
//        arrayListOf("实时热销榜","全天热销榜").forEach {
//            bangdan_tablayout?.newTab()?.setText(it)?.let { it1 -> bangdan_tablayout?.addTab(it1) }
//        }
        val pageAdapter = FragmentPagerAdapter<AppFragment<*>>(this)
        pageAdapter.addFragment(BangdanVpFragment.newInstance(1),"实时热销榜")
        pageAdapter.addFragment(BangdanVpFragment.newInstance(2),"全天热销榜")
        bangdan_vp?.adapter = pageAdapter
        bangdan_tablayout?.setupWithViewPager(bangdan_vp)
    }

    override fun initData() {

    }
}