package com.ttsq.mobile.ui.fragment

import android.os.Bundle
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.ttsq.mobile.R
import com.ttsq.mobile.action.StatusAction
import com.ttsq.mobile.app.AppFragment
import com.ttsq.mobile.ui.activity.MyOrderActivity
import com.ttsq.mobile.widget.StatusLayout

/**
 * @ClassName: OrderVpFragment
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/04 0004 13:35
 **/
class OrderVpFragment : AppFragment<MyOrderActivity>(), StatusAction {

    companion object {
        val TYPE_FLAG = "TYPE_FLAG"

        fun newInstance(type: Int): OrderVpFragment {
            val orderVpFragment = OrderVpFragment()
            val bundle = Bundle()
            bundle.putInt(TYPE_FLAG, type)
            orderVpFragment.arguments = bundle
            return orderVpFragment
        }
    }

    private val refresh: SmartRefreshLayout? by lazy { findViewById(R.id.refresh) }
    private val status_layout: StatusLayout? by lazy { findViewById(R.id.status_layout) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_order_vp
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun getStatusLayout(): StatusLayout? {
        return status_layout
    }
}