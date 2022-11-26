package com.shengqianjun.mobile.ui.fragment

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.hjq.base.FragmentPagerAdapter
import com.shengqianjun.mobile.R
import com.shengqianjun.mobile.app.AppFragment
import com.shengqianjun.mobile.app.AppHelper
import com.shengqianjun.mobile.eventbus.RefreshClass
import com.shengqianjun.mobile.ui.activity.HomeActivity
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *
 */
class BangdanVpFragment : AppFragment<HomeActivity>() {

    companion object {
        val TYPE_FLAG = "TYPE_FLAG"

        fun newInstance(type:Int): BangdanVpFragment {
            val bangdanVpFragment = BangdanVpFragment()
            val bundle = Bundle()
            bundle.putInt(TYPE_FLAG, type)
            bangdanVpFragment.arguments = bundle
            return bangdanVpFragment
        }
    }


    private val tablayout: TabLayout? by lazy { findViewById(R.id.tablayout) }
    private val vp: ViewPager? by lazy { findViewById(R.id.vp) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_bangdan_vp
    }

    override fun initView() {
        val type = getInt(TYPE_FLAG, 1)

        val bangdanAdapter = FragmentPagerAdapter<AppFragment<*>>(this)
        AppHelper.bigClassList.forEach {
            tablayout?.newTab()?.setText(it.main_name)?.let { it1 -> tablayout?.addTab(it1) }
            bangdanAdapter.addFragment(BangdanListFragment.newInstance(type,it.cid),it.main_name)
        }
        vp?.adapter = bangdanAdapter
        tablayout?.setupWithViewPager(vp)
    }

    override fun initData() {

    }
}