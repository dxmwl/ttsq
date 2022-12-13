package com.shengqiangou.mobile.ui.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.shengqiangou.mobile.R
import com.shengqiangou.mobile.app.AppFragment
import com.shengqiangou.mobile.http.api.PyqGoodsApi
import com.shengqiangou.mobile.http.model.HttpData
import com.shengqiangou.mobile.ui.activity.HomeActivity
import com.shengqiangou.mobile.ui.adapter.PqyGoodsAdapter

/**
 * 朋友圈
 */
class PyqFragment : AppFragment<HomeActivity>() {

    companion object {
        val TYPE_FLAG = "TYPE_FLAG"

        fun newInstance(): PyqFragment {
            val bangdanVpFragment = PyqFragment()
            val bundle = Bundle()
//            bundle.putInt(TYPE_FLAG, type)
            bangdanVpFragment.arguments = bundle
            return bangdanVpFragment
        }
    }

    private lateinit var pqyGoodsAdapter: PqyGoodsAdapter
    private val refresh: SmartRefreshLayout? by lazy { findViewById(R.id.refresh) }
    private val pengyouquan_list: RecyclerView? by lazy { findViewById(R.id.pengyouquan_list) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_pyq
    }

    override fun initView() {
        pengyouquan_list?.let {
            it.layoutManager = LinearLayoutManager(requireContext())
            pqyGoodsAdapter = PqyGoodsAdapter(requireContext())
            it.adapter = pqyGoodsAdapter
        }
        refresh?.setOnRefreshListener {
            pageIndex = 1
            getPyqGoods()
        }
        refresh?.setOnLoadMoreListener {
            pageIndex++
            getPyqGoods()
        }
    }

    override fun initData() {
        getPyqGoods()
    }

    private var pageIndex = 1

    private fun getPyqGoods() {
        EasyHttp.get(this)
            .api(PyqGoodsApi().apply {
                min_id = pageIndex
            })
            .request(object : OnHttpListener<HttpData<ArrayList<PyqGoodsApi.PyqGoodsDto>>> {
                override fun onSucceed(result: HttpData<ArrayList<PyqGoodsApi.PyqGoodsDto>>?) {
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                    result?.getData()?.let {
                        if (pageIndex == 1) {
                            pqyGoodsAdapter.setData(it)
                        } else {
                            pqyGoodsAdapter.addData(it)
                        }
                    }
                }

                override fun onFail(e: Exception?) {
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                    toast(e?.message)
                }
            })
    }
}