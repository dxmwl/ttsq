package com.easybuy.mobile.ui.fragment

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.easybug.mobile.R
import com.easybuy.mobile.app.TitleBarFragment
import com.easybuy.mobile.http.api.HomeGoodsListApi
import com.easybuy.mobile.http.api.QuantianBangdanApi
import com.easybuy.mobile.http.api.ShishiBangdanApi
import com.easybuy.mobile.http.model.HttpData
import com.easybuy.mobile.ui.activity.HomeActivity
import com.easybuy.mobile.ui.activity.SettingActivity
import com.easybuy.mobile.ui.adapter.BangdanGoodsListAdapter
import com.easybuy.mobile.ui.adapter.HomeGoodsListAdapter
import com.easybuy.mobile.ui.adapter.SearchGoodsListAdapter
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 我的 Fragment
 */
class MineFragment : TitleBarFragment<HomeActivity>() {

    companion object {

        fun newInstance(): MineFragment {
            return MineFragment()
        }
    }

    private var homeGoodsListAdapter: SearchGoodsListAdapter? = null
    private var homeGoodsListAdapterShishi: BangdanGoodsListAdapter? = null
    private var homeGoodsListAdapterQuantian: BangdanGoodsListAdapter? = null
    private val goodsList: RecyclerView? by lazy { findViewById(R.id.goods_list) }
    private val goods_list_shishi: RecyclerView? by lazy { findViewById(R.id.goods_list_shishi) }
    private val goods_list_quantian: RecyclerView? by lazy { findViewById(R.id.goods_list_quantian) }
    private val refresh: SmartRefreshLayout? by lazy { findViewById(R.id.refresh) }

    override fun getLayoutId(): Int {
        return R.layout.mine_fragment
    }

    override fun initView() {
        goodsList?.let {
            it.layoutManager = GridLayoutManager(context, 2)
            homeGoodsListAdapter = context?.let { it1 -> SearchGoodsListAdapter(it1) }
            it.adapter = homeGoodsListAdapter
        }

        goods_list_shishi?.let {
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            homeGoodsListAdapterShishi = context?.let { it1 -> BangdanGoodsListAdapter(it1) }
            it.adapter = homeGoodsListAdapterShishi
        }
        goods_list_quantian?.let {
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            homeGoodsListAdapterQuantian = context?.let { it1 -> BangdanGoodsListAdapter(it1) }
            it.adapter = homeGoodsListAdapterQuantian
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
        getGoodsListShishi()
        getGoodsListQuantian()
    }

    private fun getGoodsListQuantian() {
        EasyHttp.get(this)
            .api(QuantianBangdanApi())
            .request(object : OnHttpListener<HttpData<ArrayList<HomeGoodsListApi.GoodsBean>>> {
                override fun onSucceed(result: HttpData<ArrayList<HomeGoodsListApi.GoodsBean>>?) {
                    homeGoodsListAdapterQuantian?.setData(result?.getData())
                }

                override fun onFail(e: java.lang.Exception?) {
                    toast(e?.message)
                }

            })
    }

    private fun getGoodsListShishi() {
        EasyHttp.get(this)
            .api(ShishiBangdanApi())
            .request(object : OnHttpListener<HttpData<ArrayList<HomeGoodsListApi.GoodsBean>>> {
                override fun onSucceed(result: HttpData<ArrayList<HomeGoodsListApi.GoodsBean>>?) {
                    homeGoodsListAdapterShishi?.setData(result?.getData())
                }

                override fun onFail(e: java.lang.Exception?) {
                    toast(e?.message)
                }

            })
    }

    /**
     * 获取商品列表
     */
    private fun getGoodsList() {
        EasyHttp.get(this)
            .api(HomeGoodsListApi().apply {
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

    override fun onRightClick(view: View) {
        super.onRightClick(view)
        startActivity(SettingActivity::class.java)
    }

    override fun isStatusBarEnabled(): Boolean {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled()
    }
}