package com.easybuy.mobile.ui.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.easybug.mobile.R
import com.easybuy.mobile.app.AppHelper
import com.easybuy.mobile.app.TitleBarFragment
import com.easybuy.mobile.ui.activity.HomeActivity
import com.easywidgets.tablayout.EasyTabLayout

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 友圈爆款 Fragment
 */
class YqbkFragment : TitleBarFragment<HomeActivity>() {

    companion object {

        fun newInstance(): YqbkFragment {
            return YqbkFragment()
        }
    }

    private val tab_yqbk: EasyTabLayout? by lazy { findViewById(R.id.tab_yqbk) }
    private val vp_yqbk: ViewPager? by lazy { findViewById(R.id.vp_yqbk) }

    override fun getLayoutId(): Int {
        return R.layout.yqbk_fragment
    }

    override fun initView() {

    }

    override fun initData() {
        initTabList()
    }

    override fun isStatusBarEnabled(): Boolean {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled()
    }

    /**
     * 加载tabs
     */
    private fun initTabList() {
        val classData = AppHelper.classData
        val fragmentList = arrayListOf<Fragment>()
        val titleList = arrayOfNulls<String>(classData.size)
        classData.forEachIndexed { index, s ->
            titleList[index] = s.name
            fragmentList.add(YqbkListFragment(s.cid))
        }

        vp_yqbk?.adapter = VpAdapter(fragmentList,titleList)
        tab_yqbk?.setViewPager(vp_yqbk, titleList)
    }

    inner class VpAdapter(val fragments: ArrayList<Fragment>, val titleList: Array<String?>) :
        FragmentStatePagerAdapter(childFragmentManager) {
        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titleList[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

    }
}