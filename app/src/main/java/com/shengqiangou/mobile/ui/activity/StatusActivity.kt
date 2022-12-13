package com.shengqiangou.mobile.ui.activity

import androidx.core.content.ContextCompat
import com.hjq.base.BaseDialog
import com.shengqiangou.mobile.R
import com.shengqiangou.mobile.action.StatusAction
import com.shengqiangou.mobile.app.AppActivity
import com.shengqiangou.mobile.ui.dialog.MenuDialog
import com.shengqiangou.mobile.widget.StatusLayout
import com.shengqiangou.mobile.widget.StatusLayout.OnRetryListener

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2019/04/17
 *    desc   : 加载使用案例
 */
class StatusActivity : AppActivity(), com.shengqiangou.mobile.action.StatusAction {

    private val hintLayout: StatusLayout? by lazy { findViewById(R.id.hl_status_hint) }

    override fun getLayoutId(): Int {
        return R.layout.status_activity
    }

    override fun initView() {

    }

    override fun initData() {
        MenuDialog.Builder(this) //.setAutoDismiss(false) // 设置点击按钮后不关闭对话框
            .setList("加载中", "请求错误", "空数据提示", "自定义提示")
            .setListener(object : MenuDialog.OnListener<String> {

                override fun onSelected(dialog: BaseDialog?, position: Int, data: String) {
                    when (position) {
                        0 -> {
                            showLoading()
                            postDelayed({ showComplete() }, 2500)
                        }
                        1 -> {
                            showError(object : OnRetryListener {
                                override fun onRetry(layout: StatusLayout) {
                                    showLoading()
                                    postDelayed({ showEmpty() }, 2500)
                                }
                            })
                        }
                        2 -> showEmpty()
                        3 -> {
                            showLayout(ContextCompat.getDrawable(this@StatusActivity,
                                    R.drawable.status_order_ic), "暂无订单", null)
                        }
                    }
                }
            })
            .show()
    }

    override fun getStatusLayout(): StatusLayout? {
        return hintLayout
    }
}