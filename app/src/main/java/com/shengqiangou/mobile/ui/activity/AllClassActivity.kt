package com.shengqiangou.mobile.ui.activity

import com.shengqiangou.mobile.R
import com.shengqiangou.mobile.app.AppActivity
import com.shengqiangou.mobile.ui.fragment.ZtkClassFragment

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