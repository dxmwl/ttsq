package com.shengqiangou.mobile.ui.fragment

import com.shengqiangou.mobile.R
import com.shengqiangou.mobile.app.AppFragment
import com.shengqiangou.mobile.ui.activity.CopyActivity

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 可进行拷贝的副本
 */
class CopyFragment : AppFragment<CopyActivity>() {

    companion object {

        fun newInstance(): CopyFragment {
            return CopyFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.copy_fragment
    }

    override fun initView() {}

    override fun initData() {}
}