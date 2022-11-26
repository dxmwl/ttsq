package com.shengqianjun.mobile.ui.activity

import com.shengqianjun.mobile.R
import com.shengqianjun.mobile.app.AppActivity
import com.shengqianjun.mobile.ui.fragment.ZtkClassFragment

/**
 * 全部分类
 */
class AllClassActivity : AppActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_all_class
    }

    override fun initView() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_content, ZtkClassFragment.newInstance()).commit()
    }

    override fun initData() {

    }
}