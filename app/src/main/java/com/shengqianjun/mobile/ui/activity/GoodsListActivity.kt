package com.shengqianjun.mobile.ui.activity

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shengqianjun.mobile.R
import com.shengqianjun.mobile.aop.Log
import com.shengqianjun.mobile.app.AppActivity
import com.shengqianjun.mobile.http.api.GoodsListApi
import com.shengqianjun.mobile.http.api.HomeGoodsListApi
import com.shengqianjun.mobile.http.model.HttpData
import com.shengqianjun.mobile.ui.adapter.GoodsListAdapter
import com.shengqianjun.mobile.utils.StringUtils
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.scwang.smart.refresh.layout.SmartRefreshLayout


/**
 * @project : EasyBuy_Android
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/6/23
 */
class GoodsListActivity : AppActivity() {

    companion object {

        private const val URL_PATH: String = "URL_PATH"
        private const val TITLE_STR: String = "TITLE_STR"

        @Log
        fun start(mContext: Context, urlPath: String?, titleStr: String?) {
            val intent = Intent(mContext, GoodsListActivity::class.java)
            intent.putExtra(URL_PATH, urlPath)
            intent.putExtra(TITLE_STR, titleStr)
            mContext.startActivity(intent)
        }
    }

    private val refresh: SmartRefreshLayout? by lazy { findViewById(R.id.refresh) }
    private lateinit var homeGoodsListAdapter: GoodsListAdapter
    private val goodsList: RecyclerView? by lazy { findViewById(R.id.goods_list) }

    override fun getLayoutId(): Int {
        return R.layout.activity_goods_list
    }

    override fun initView() {
        title = getString(TITLE_STR)
        goodsList?.let {
            it.layoutManager = LinearLayoutManager(this)
            homeGoodsListAdapter = GoodsListAdapter(this)
            it.adapter = homeGoodsListAdapter
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

    private var pageIndex = 1

    override fun initData() {
        getGoodsList()
    }

    private fun getGoodsList() {
        val urlPath = getString(URL_PATH)!!
        EasyHttp.get(this)
            .api(GoodsListApi(StringUtils.changeParamForKey(urlPath, "page", "$pageIndex")))
            .request(object : OnHttpListener<HttpData<ArrayList<HomeGoodsListApi.GoodsBean>>> {
                override fun onSucceed(result: HttpData<ArrayList<HomeGoodsListApi.GoodsBean>>?) {
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                    homeGoodsListAdapter.addData(result?.getData())
                }

                override fun onFail(e: Exception?) {
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                    toast(e?.message)
                }
            })
    }
}