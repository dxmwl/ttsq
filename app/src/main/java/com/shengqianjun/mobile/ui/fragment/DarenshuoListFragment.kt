package com.shengqianjun.mobile.ui.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.shengqianjun.mobile.R
import com.shengqianjun.mobile.app.AppFragment
import com.shengqianjun.mobile.http.api.DarenshuoApi
import com.shengqianjun.mobile.http.model.HttpData
import com.shengqianjun.mobile.ui.activity.HomeActivity
import com.shengqianjun.mobile.ui.adapter.DarenshuoAdapter
import com.shengqianjun.mobile.ui.adapter.PqyGoodsAdapter
import java.lang.Exception

/**
 * 达人说
 */
class DarenshuoListFragment: AppFragment<HomeActivity>() {

    companion object {

        val TYPE_FLAG = "TYPE_FLAG"

        fun newInstance(type: Int): DarenshuoListFragment {
            val bangdanListFragment = DarenshuoListFragment()
            val bundle = Bundle()
            bundle.putInt(TYPE_FLAG, type)
            bangdanListFragment.arguments = bundle
            return bangdanListFragment
        }
    }

    private var typeInt: Int = 0
    private lateinit var pqyGoodsAdapter: DarenshuoAdapter
    private val refresh: SmartRefreshLayout? by lazy { findViewById(R.id.refresh) }
    private val pengyouquan_list: RecyclerView? by lazy { findViewById(R.id.pengyouquan_list) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_darenshuo_list
    }

    override fun initView() {
        typeInt = getInt(TYPE_FLAG,0)
        pengyouquan_list?.let {
            it.layoutManager = LinearLayoutManager(requireContext())
            pqyGoodsAdapter = DarenshuoAdapter(requireContext())
            it.adapter = pqyGoodsAdapter
        }
    }

    override fun initData() {

        getDarenshuo()
    }

    private fun getDarenshuo() {
        EasyHttp.get(this)
            .api(DarenshuoApi().apply {
                talentcat = typeInt
            })
            .request(object :OnHttpListener<HttpData<DarenshuoApi.DarenshuoListDto>>{
                override fun onSucceed(result: HttpData<DarenshuoApi.DarenshuoListDto>?) {
                    result?.getData()?.let {
                        pqyGoodsAdapter.setData(it.newdata)
                    }
                }

                override fun onFail(e: Exception?) {
                    toast(e?.message)
                }

            })
    }
}