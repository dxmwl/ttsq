package com.ttsq.mobile.ui.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.ttsq.mobile.R
import com.ttsq.mobile.app.AppFragment
import com.ttsq.mobile.http.api.ClassApi
import com.ttsq.mobile.http.api.GetDyGoodsListApi
import com.ttsq.mobile.http.model.HttpData
import com.ttsq.mobile.manager.UserManager
import com.ttsq.mobile.ui.activity.HomeActivity
import com.ttsq.mobile.ui.adapter.GoodsListAdapter
import com.ttsq.mobile.ui.adapter.GoodsListDyAdapter
import java.lang.Exception

/**
 * @ClassName: DyGoodsFragment
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/22 0022 13:56
 **/
class DyGoodsFragment : AppFragment<HomeActivity>() {

    private lateinit var goodsListDyAdapter: GoodsListDyAdapter
    private var cidCode: Int = 0

    companion object {

        val CIDFLAG = "cid"

        fun newInstance(cid: Int): DyGoodsFragment {
            val dyGoodsFragment = DyGoodsFragment()
            val bundle = Bundle()
            bundle.putInt(CIDFLAG, cid)
            dyGoodsFragment.arguments = bundle
            return dyGoodsFragment
        }
    }

    private val goods_list: RecyclerView? by lazy { findViewById(R.id.goods_list) }
    private val refresh: SmartRefreshLayout? by lazy { findViewById(R.id.refresh) }
    private var pageNum = 1

    override fun getLayoutId(): Int {
        return R.layout.fragment_dy_goods
    }

    override fun initView() {
        cidCode = getInt(CIDFLAG)

        goods_list?.also {
            it.layoutManager = LinearLayoutManager(requireContext())
            goodsListDyAdapter = GoodsListDyAdapter(requireContext())
            it.adapter = goodsListDyAdapter
        }

        refresh?.setOnRefreshListener {
            pageNum = 1
            getGoodsList()
        }
        refresh?.setOnLoadMoreListener {
//            pageNum++
            getGoodsList()
        }
    }

    override fun initData() {
        getGoodsList()
    }

    private fun getGoodsList() {
        EasyHttp.get(this)
            .api(GetDyGoodsListApi().apply {
                category_id = cidCode
                city_code = UserManager.currentLocation?.adCode
                longitude = UserManager.currentLocation?.longitude?.toString()
                latitude = UserManager.currentLocation?.latitude?.toString()
                min_id = pageNum
            })
            .request(object : OnHttpListener<HttpData<ArrayList<GetDyGoodsListApi.DyGoodsDto>>> {
                override fun onSucceed(result: HttpData<ArrayList<GetDyGoodsListApi.DyGoodsDto>>?) {
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                    pageNum = result?.getMinId() ?: 1
                    result?.getData()?.let {
                        if (pageNum == 1) {
                            goodsListDyAdapter.clearData()
                        }
                        goodsListDyAdapter.addData(it)
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