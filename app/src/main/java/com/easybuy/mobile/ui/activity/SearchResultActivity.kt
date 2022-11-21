package com.easybuy.mobile.ui.activity

import android.view.View
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.easybug.mobile.R
import com.easybuy.mobile.aop.SingleClick
import com.easybuy.mobile.app.AppActivity
import com.easybuy.mobile.app.AppHelper
import com.easybuy.mobile.http.api.HomeGoodsListApi
import com.easybuy.mobile.http.api.SearchGoodsApi
import com.easybuy.mobile.http.model.HttpData
import com.easybuy.mobile.ui.adapter.SearchGoodsListAdapter
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * @project : EasyBuy_Android
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/6/23
 */
class SearchResultActivity : AppActivity() {
    private var pageIndex: Int = 1
    private var keyword: String = ""

    private var homeGoodsListAdapter: SearchGoodsListAdapter? = null
    private val goodsList: RecyclerView? by lazy { findViewById(R.id.goods_list) }
    private val refresh: SmartRefreshLayout? by lazy { findViewById(R.id.refresh) }
    private val input_keyword: EditText? by lazy { findViewById(R.id.input_keyword) }

    override fun getLayoutId(): Int {
        return R.layout.activity_search_result
    }

    override fun initView() {
        setOnClickListener(R.id.btn_search, R.id.iv_back)
        keyword = intent.getStringExtra("KEYWORD").toString()
        input_keyword?.setText(keyword)
        input_keyword?.setSelection(keyword.length)

        input_keyword?.addTextChangedListener {
            keyword = it?.toString().toString()
        }

        goodsList?.let {
            it.layoutManager = GridLayoutManager(this, 2)
            homeGoodsListAdapter = SearchGoodsListAdapter(this)
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
        AppHelper.saveSearchHistory(keyword)
        EasyHttp.get(this)
            .api(SearchGoodsApi().apply {
                q = keyword
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

    @SingleClick
    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.iv_back -> {
                finish()
            }
            R.id.btn_search -> {
                pageIndex = 1
                searchGoods()
            }
            else -> {}
        }
    }
}