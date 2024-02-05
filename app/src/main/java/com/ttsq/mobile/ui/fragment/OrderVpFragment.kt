package com.ttsq.mobile.ui.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.ttsq.mobile.R
import com.ttsq.mobile.action.StatusAction
import com.ttsq.mobile.app.AppFragment
import com.ttsq.mobile.http.api.GetShoppingTaobaoOrderListApi
import com.ttsq.mobile.http.model.HttpData
import com.ttsq.mobile.ui.activity.MyOrderActivity
import com.ttsq.mobile.ui.adapter.ShoppingOrderTaobaoAdapter
import com.ttsq.mobile.widget.StatusLayout
import java.lang.Exception

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

    private lateinit var shoppingOrderTaobaoAdapter: ShoppingOrderTaobaoAdapter
    private val refresh: SmartRefreshLayout? by lazy { findViewById(R.id.refresh) }
    private val status_layout: StatusLayout? by lazy { findViewById(R.id.status_layout) }
    private val order_list: RecyclerView? by lazy { findViewById(R.id.order_list) }
    private var pageNum = 1

    override fun getLayoutId(): Int {
        return R.layout.fragment_order_vp
    }

    override fun initView() {

        refresh?.setOnRefreshListener {
            pageNum = 1
            getShoppingTaobaoOrderList()
        }
        refresh?.setOnLoadMoreListener {
            pageNum++
            getShoppingTaobaoOrderList()
        }

        order_list?.also {
            it.layoutManager = LinearLayoutManager(requireContext())
            shoppingOrderTaobaoAdapter = ShoppingOrderTaobaoAdapter(requireContext())
            it.adapter = shoppingOrderTaobaoAdapter
        }
    }

    override fun initData() {
        getShoppingTaobaoOrderList()
    }

    private fun getShoppingTaobaoOrderList() {
        EasyHttp.post(this)
            .api(GetShoppingTaobaoOrderListApi().apply {
                this.pageNum = this@OrderVpFragment.pageNum
                this.status = getInt(TYPE_FLAG)
            })
            .request(object :
                OnHttpListener<HttpData<ArrayList<GetShoppingTaobaoOrderListApi.TaobaoShoppingOrderDto>>> {
                override fun onSucceed(result: HttpData<ArrayList<GetShoppingTaobaoOrderListApi.TaobaoShoppingOrderDto>>?) {
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                    result?.getData()?.let {
                        if (pageNum == 1) {
                            shoppingOrderTaobaoAdapter.clearData()
                        }
                        shoppingOrderTaobaoAdapter.addData(it)
                    }

                    if (shoppingOrderTaobaoAdapter.getCount() == 0) {
                        showEmpty()
                    } else {
                        showComplete()
                    }
                }

                override fun onFail(e: Exception?) {
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                    toast(e?.message)
                }

            })
    }

    override fun getStatusLayout(): StatusLayout? {
        return status_layout
    }
}