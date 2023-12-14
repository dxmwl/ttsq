package com.ttsq.mobile.ui.activity

import android.view.View
import android.widget.TextView
import com.ttsq.mobile.R
import com.ttsq.mobile.app.AppActivity
import com.ttsq.mobile.other.AppConfig

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 关于界面
 */
class AboutActivity : AppActivity() {

    private val app_version: TextView? by lazy { findViewById(R.id.app_version) }

    override fun getLayoutId(): Int {
        return R.layout.about_activity
    }

    override fun initView() {
        setOnClickListener(R.id.tv_beian)
        app_version?.text = AppConfig.getVersionName()
    }

    override fun initData() {}

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tv_beian -> {
                BrowserActivity.start(this,"https://beian.miit.gov.cn")
            }
            else -> {}
        }
    }
}