package com.shengqianjun.mobile.ui.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shengqianjun.mobile.R
import com.shengqianjun.mobile.app.AppFragment
import com.shengqianjun.mobile.http.api.YqbkApi
import com.shengqianjun.mobile.http.model.HttpData
import com.shengqianjun.mobile.ui.activity.HomeActivity
import com.shengqianjun.mobile.ui.adapter.YqbkListAdapter
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class YqbkListFragment(private val cidCode: String) : AppFragment<HomeActivity>() {
    private var pageNum = 1
    private lateinit var adapter: YqbkListAdapter

    private val yqbk_list: RecyclerView? by lazy { findViewById(R.id.yqbk_list) }
    private val refresh: SmartRefreshLayout? by lazy { findViewById(R.id.refresh) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_yqbk_list
    }

    override fun initView() {
        yqbk_list?.also {
            it.layoutManager = LinearLayoutManager(context)
            adapter = YqbkListAdapter(requireContext())
            it.adapter = adapter
        }

        refresh?.setOnRefreshListener {
            pageNum = 1
            getGoodsList()
        }
        refresh?.setOnLoadMoreListener {
            pageNum++
            getGoodsList()
        }
    }

    override fun initData() {
        getGoodsList()
    }

    private fun getGoodsList() {
        EasyHttp.get(this)
            .api(YqbkApi().apply {
                page = pageNum
                this.cid = cidCode
            })
            .request(object : OnHttpListener<HttpData<ArrayList<YqbkApi.YqbkGoodsInfo>>> {
                override fun onSucceed(result: HttpData<ArrayList<YqbkApi.YqbkGoodsInfo>>?) {
                    refresh?.finishRefresh(500)
                    refresh?.finishLoadMore(500)
                    result?.getData()?.let {
                        if (pageNum == 1) {
                            adapter.setData(it)
                        } else {
                            adapter.addData(it)
                        }
                    }

                }

                override fun onFail(e: Exception?) {
                    toast(e?.message)
                    refresh?.finishRefresh(500)
                    refresh?.finishLoadMore(500)
                }
            })
    }
}