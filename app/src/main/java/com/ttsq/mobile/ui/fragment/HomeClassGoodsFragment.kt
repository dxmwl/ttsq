package com.ttsq.mobile.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.ttsq.mobile.R
import com.ttsq.mobile.app.AppFragment
import com.ttsq.mobile.http.api.ClassApi
import com.ttsq.mobile.http.api.CommodityScreeningApi
import com.ttsq.mobile.http.model.HttpData
import com.ttsq.mobile.http.model.MenuDto
import com.ttsq.mobile.ui.activity.HomeActivity
import com.ttsq.mobile.ui.activity.SearchResultActivity
import com.ttsq.mobile.ui.adapter.HdkTwoClassAdapter
import com.ttsq.mobile.ui.adapter.SearchGoodsListAdapter
import com.ttsq.mobile.ui.adapter.ShaixuanGoodsListAdapter
import java.lang.Exception


class HomeClassGoodsFragment : AppFragment<HomeActivity>(), HdkTwoClassAdapter.OnItemClickListener {

    private var cidStr: String = ""
    private var childData: ArrayList<ClassApi.Data> = ArrayList()

    companion object {

        val DATAFLAG = "DATAFLAG"
        val CIDFLAG = "cid"

        fun newInstance(data: ArrayList<ClassApi.Data>,cid:String): HomeClassGoodsFragment {
            val homeClassGoodsFragment = HomeClassGoodsFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList(DATAFLAG, data)
            bundle.putString(CIDFLAG, cid)
            homeClassGoodsFragment.arguments = bundle
            return homeClassGoodsFragment
        }
    }

    private var homeGoodsListAdapter: ShaixuanGoodsListAdapter? = null
    private lateinit var threeClassAdapter: HdkTwoClassAdapter.ThreeClassAdapter
    private val goods_class: RecyclerView? by lazy { findViewById(R.id.goods_class) }
    private val goods_sort: TabLayout? by lazy { findViewById(R.id.goods_sort) }
    private val goodsList: RecyclerView? by lazy { findViewById(R.id.goods_list) }
    private val refresh: SmartRefreshLayout? by lazy { findViewById(R.id.refresh) }
    private var isYouquan: Int = 1
    private var paixu: String = "new"
    private var pageIndex: Int = 0

    override fun getLayoutId(): Int {
        return R.layout.fragment_home_goods
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val bundle = arguments
        if (null != bundle) {
            cidStr = bundle.getString(CIDFLAG).toString()
            childData =
                bundle.getParcelableArrayList<ClassApi.Data>(DATAFLAG) as ArrayList<ClassApi.Data>
        }

    }

    override fun initView() {
        goods_class?.let {
            it.layoutManager = GridLayoutManager(requireContext(), 5)
            threeClassAdapter = HdkTwoClassAdapter.ThreeClassAdapter(requireContext(), this)
            it.adapter = threeClassAdapter
        }

        val listData = arrayListOf(
            MenuDto(title = "综合", checked = true, value = "0"),
            MenuDto(title = "销量", checked = false, value = "4"),
            MenuDto(title = "价格", checked = false, value = "1"),
            MenuDto(title = "优惠", checked = false, value = "13"),
        )
        listData.forEach {
            goods_sort?.newTab()?.setText(it.title)?.let { it1 -> goods_sort?.addTab(it1) }
        }
        goods_sort?.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                paixu = listData[tab?.position!!].value.toString()
                pageIndex = 1
                searchGoods()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        goodsList?.let {
            it.layoutManager = LinearLayoutManager(requireContext())
            homeGoodsListAdapter = ShaixuanGoodsListAdapter(requireContext())
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
        val classList = ArrayList<ClassApi.Info>()
        childData.forEach {
            classList.addAll(it.info)
        }
        val tempList = if (classList.size > 10) {
            classList.subList(0, 10)
        }else{
            classList
        }
        threeClassAdapter.setData(tempList)
        searchGoods()
    }

    override fun onItemClick(classDataBean: ClassApi.Info) {
        val intent = Intent(getAttachActivity(), SearchResultActivity::class.java)
        intent.putExtra("KEYWORD", classDataBean.son_name)
        startActivity(intent)
    }

    private fun searchGoods() {
        EasyHttp.get(this)
            .api(CommodityScreeningApi().apply {
                cid = cidStr
                min_id = pageIndex
                sort = paixu
            })
            .request(object :OnHttpListener<HttpData<ArrayList<CommodityScreeningApi.ShaixuanGoodsDto>>>{
                override fun onSucceed(result: HttpData<ArrayList<CommodityScreeningApi.ShaixuanGoodsDto>>?) {
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
                }

            })
    }

}