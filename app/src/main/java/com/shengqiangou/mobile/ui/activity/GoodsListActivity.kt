package com.shengqiangou.mobile.ui.activity

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.shengqiangou.mobile.R
import com.shengqiangou.mobile.aop.Log
import com.shengqiangou.mobile.app.AppActivity
import com.shengqiangou.mobile.http.api.GoodsListApi
import com.shengqiangou.mobile.http.model.HttpData
import com.shengqiangou.mobile.ui.adapter.GoodsListAdapter


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
    private val home_tab: TabLayout? by lazy { findViewById(R.id.home_tab) }

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


        home_tab?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                homeGoodsListAdapter.setData(goodsInfoDto[tab?.position!!].item)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

    private var pageIndex = 1

    override fun initData() {
        getGoodsList()
    }

    private var goodsInfoDto: ArrayList<GoodsListApi.Block> = ArrayList()

    private fun getGoodsList() {
        EasyHttp.get(this)
            .api(GoodsListApi().apply {
                id = getString(URL_PATH).toString()
            })
            .request(object : OnHttpListener<HttpData<GoodsListApi.HuodongGoodsDto>> {
                override fun onSucceed(result: HttpData<GoodsListApi.HuodongGoodsDto>?) {
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                    result?.getData()?.let { it ->
                        goodsInfoDto = it.block
                        goodsInfoDto.forEach { it1 ->
                            val tab = home_tab?.newTab()?.setText(it1.name)
                            if (tab != null) {
                                home_tab?.addTab(tab)
                            }
                        }
                        homeGoodsListAdapter.setData(goodsInfoDto[0].item)
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