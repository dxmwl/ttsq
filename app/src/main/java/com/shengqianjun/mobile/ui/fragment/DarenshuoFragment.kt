package com.shengqianjun.mobile.ui.fragment

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.hjq.base.FragmentPagerAdapter
import com.shengqianjun.mobile.R
import com.shengqianjun.mobile.app.AppFragment
import com.shengqianjun.mobile.ui.activity.HomeActivity

/**
 * 达人说
 */
class DarenshuoFragment : AppFragment<HomeActivity>() {

    companion object {
        val TYPE_FLAG = "TYPE_FLAG"

        fun newInstance(): DarenshuoFragment {
            val bangdanVpFragment = DarenshuoFragment()
            val bundle = Bundle()
//            bundle.putInt(TYPE_FLAG, type)
            bangdanVpFragment.arguments = bundle
            return bangdanVpFragment
        }
    }

    private val tablayout: TabLayout? by lazy { findViewById(R.id.tablayout) }
    private val vp: ViewPager? by lazy { findViewById(R.id.vp) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_darenshuo
    }

    override fun initView() {

        val bangdanAdapter = FragmentPagerAdapter<AppFragment<*>>(this)
        arrayListOf(
            ItemType(0, "全部"),
            ItemType(1, "好物"),
            ItemType(2, "潮流"),
            ItemType(3, "美食"),
            ItemType(4, "生活")
        ).forEach {
            bangdanAdapter.addFragment(DarenshuoListFragment.newInstance(it.type), it.typeStr)
        }
        vp?.adapter = bangdanAdapter
        tablayout?.setupWithViewPager(vp)
    }

    override fun initData() {

    }

    data class ItemType(
        val type: Int,
        val typeStr: String,
    )
}