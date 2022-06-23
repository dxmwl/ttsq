package com.easybuy.mobile.ui.activity

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.easybug.mobile.R
import com.easybuy.mobile.app.AppActivity
import com.easybuy.mobile.http.api.HomeGoodsListApi
import com.easybuy.mobile.http.api.SearchGoodsApi
import com.easybuy.mobile.http.model.HttpData
import com.easybuy.mobile.ui.adapter.HomeGoodsListAdapter
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * @project : EasyBuy_Android
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/6/23
 */
class SearchResultActivity:AppActivity() {
    private var pageIndex: Int = 1
    private var keyword: String? = ""

    private var homeGoodsListAdapter: HomeGoodsListAdapter? = null
    private val goodsList: RecyclerView? by lazy { findViewById(R.id.goods_list) }
    private val refresh: SmartRefreshLayout? by lazy { findViewById(R.id.refresh) }

    override fun getLayoutId(): Int {
        return R.layout.activity_search_result
    }

    override fun initView() {
        keyword = intent.getStringExtra("KEYWORD")

        goodsList?.let {
            it.layoutManager = GridLayoutManager(this, 2)
            homeGoodsListAdapter = HomeGoodsListAdapter(this)
            it.adapter = homeGoodsListAdapter
        }

        refresh?.setOnRefreshListener {
            pageIndex = 1
            searchGoods()
        }
        refresh?.setOnLoadMoreListener {
            pageIndex++
            searchGoods()
        }
    }

    override fun initData() {

        searchGoods()
    }

    private fun searchGoods() {
        EasyHttp.get(this)
            .api(SearchGoodsApi().apply {
                q = keyword.toString()
                page = pageIndex
            })
            .request(object : OnHttpListener<HttpData<ArrayList<HomeGoodsListApi.GoodsBean>>> {
                override fun onSucceed(result: HttpData<ArrayList<HomeGoodsListApi.GoodsBean>>?) {
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                    if (pageIndex == 1) {
                        homeGoodsListAdapter?.clearData()
                    }
                    homeGoodsListAdapter?.addData(result?.getData())
                }

                override fun onFail(e: Exception?) {
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                    toast(e?.message)
                }
            })
    }
}