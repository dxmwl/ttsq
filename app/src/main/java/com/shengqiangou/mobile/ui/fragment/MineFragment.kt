package com.shengqiangou.mobile.ui.fragment

import android.view.View
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.hjq.base.FragmentPagerAdapter
import com.shengqiangou.mobile.R
import com.shengqiangou.mobile.app.AppFragment
import com.shengqiangou.mobile.app.TitleBarFragment
import com.shengqiangou.mobile.ui.activity.HomeActivity
import com.shengqiangou.mobile.ui.activity.SettingActivity

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 我的 Fragment
 */
class MineFragment : TitleBarFragment<HomeActivity>() {

    companion object {

        fun newInstance(): MineFragment {
            return MineFragment()
        }
    }

    private val bangdan_tablayout: TabLayout? by lazy { findViewById(R.id.bangdan_tablayout) }
    private val bangdan_vp: ViewPager? by lazy { findViewById(R.id.bangdan_vp) }

    override fun getLayoutId(): Int {
        return R.layout.mine_fragment
    }

    override fun initView() {
        setOnClickListener(R.id.btn_setting)
        val pageAdapter = FragmentPagerAdapter<AppFragment<*>>(this)
        pageAdapter.addFragment(PyqFragment.newInstance(), "朋友圈")
        pageAdapter.addFragment(BangdanVpFragment.newInstance(2), "好货专场")
        pageAdapter.addFragment(DarenshuoFragment.newInstance(), "达人说")
        bangdan_vp?.adapter = pageAdapter
        bangdan_tablayout?.setupWithViewPager(bangdan_vp)
    }

    override fun initData() {
    }

    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.btn_setting -> {
                startActivity(SettingActivity::class.java)
            }
            else -> {}
        }
    }

    override fun isStatusBarEnabled(): Boolean {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled()
    }
}