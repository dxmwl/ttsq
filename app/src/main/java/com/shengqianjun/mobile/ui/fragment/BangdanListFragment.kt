package com.shengqianjun.mobile.ui.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.shengqianjun.mobile.R
import com.shengqianjun.mobile.app.AppFragment
import com.shengqianjun.mobile.http.api.BangdanGoodsApi
import com.shengqianjun.mobile.http.api.CommodityScreeningApi
import com.shengqianjun.mobile.http.model.HttpData
import com.shengqianjun.mobile.ui.activity.HomeActivity
import com.shengqianjun.mobile.ui.adapter.BangdanGoodsListAdapter
import com.shengqianjun.mobile.ui.adapter.BangdanGoodsListAdapter2

/**
 * 榜单商品列表
 */
class BangdanListFragment : AppFragment<HomeActivity>() {

    companion object {

        val TYPE_FLAG = "TYPE_FLAG"
        val CID_FLAG = "CID_FLAG"

        fun newInstance(type: Int, cid: String): BangdanListFragment {
            val bangdanListFragment = BangdanListFragment()
            val bundle = Bundle()
            bundle.putInt(TYPE_FLAG, type)
            bundle.putString(CID_FLAG, cid)
            bangdanListFragment.arguments = bundle
            return bangdanListFragment
        }
    }

    private lateinit var bangdanGoodsListAdapter: BangdanGoodsListAdapter2
    private val goods_list: RecyclerView? by lazy { findViewById(R.id.goods_list) }
    private val refresh: SmartRefreshLayout? by lazy { findViewById(R.id.refresh) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_bangdan_goods_list
    }

    override fun initView() {
        goods_list?.let {
            it.layoutManager = LinearLayoutManager(requireContext())
            bangdanGoodsListAdapter = BangdanGoodsListAdapter2(requireContext(),getInt(TYPE_FLAG))
            it.adapter = bangdanGoodsListAdapter
        }

        refresh?.setOnRefreshListener {
            pageIndex = 1
            getGoodsList()
        }
        refresh?.setOnLoadMoreListener {
            pageIndex++
            getGoodsList()
        }
    }

    override fun initData() {
        getGoodsList()
    }

    private var pageIndex = 1

    private fun getGoodsList() {
        EasyHttp.get(this)
            .api(BangdanGoodsApi().apply {
                sale_type = getInt(TYPE_FLAG)
                cid = getString(CID_FLAG).toString()
                min_id = pageIndex
            })
            .request(object :
                OnHttpListener<HttpData<ArrayList<CommodityScreeningApi.ShaixuanGoodsDto>>> {
                override fun onSucceed(result: HttpData<ArrayList<CommodityScreeningApi.ShaixuanGoodsDto>>?) {
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                    result?.getData()?.let {
                        if (pageIndex == 1) {
                            bangdanGoodsListAdapter.setData(it)
                        } else {
                            bangdanGoodsListAdapter.addData(it)
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