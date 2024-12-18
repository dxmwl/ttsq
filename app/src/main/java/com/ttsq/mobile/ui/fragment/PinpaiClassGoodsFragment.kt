package com.ttsq.mobile.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hjq.base.BaseAdapter
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.ttsq.mobile.R
import com.ttsq.mobile.app.AppFragment
import com.ttsq.mobile.http.api.ClassApi
import com.ttsq.mobile.http.api.ClassPinpaiListApi
import com.ttsq.mobile.http.model.HttpData
import com.ttsq.mobile.ui.activity.PinpaiDetailActivity
import com.ttsq.mobile.ui.activity.PinpaiGoodsActivity
import com.ttsq.mobile.ui.adapter.ClassPinpaiListAdapter
import java.lang.Exception

class PinpaiClassGoodsFragment : AppFragment<PinpaiGoodsActivity>() {

    companion object {

        val DATAFLAG = "DATAFLAG"
        val CIDFLAG = "cid"

        fun newInstance(data: ArrayList<ClassApi.Data>, cid: String): PinpaiClassGoodsFragment {
            val homeClassGoodsFragment = PinpaiClassGoodsFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList(DATAFLAG, data)
            bundle.putString(CIDFLAG, cid)
            homeClassGoodsFragment.arguments = bundle
            return homeClassGoodsFragment
        }
    }

    private var cidStr: String = ""
    private lateinit var classPinpaiListAdapter: ClassPinpaiListAdapter
    private var pageIndex: Int = 0
    private val refresh: SmartRefreshLayout? by lazy { findViewById(R.id.refresh) }
    private val item_pinpai_list:RecyclerView?by lazy { findViewById(R.id.pinpai_list) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_pinpai_class
    }

    override fun initView() {
        item_pinpai_list?.let {
            it.layoutManager = LinearLayoutManager(requireContext())
            classPinpaiListAdapter = ClassPinpaiListAdapter(requireContext())
            classPinpaiListAdapter.setOnItemClickListener(object :BaseAdapter.OnItemClickListener{
                override fun onItemClick(
                    recyclerView: RecyclerView?,
                    itemView: View?,
                    position: Int,
                ) {
                    val pinpaiListDto = classPinpaiListAdapter.getItem(position)
                    PinpaiDetailActivity.start(requireContext(),pinpaiListDto.id)
                }
            })
            it.adapter = classPinpaiListAdapter
        }
        refresh?.setOnRefreshListener {
            pageIndex = 1
            getPinpaiList()
        }
        refresh?.setOnLoadMoreListener {
            pageIndex++
            getPinpaiList()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val bundle = arguments
        if (null != bundle) {
            cidStr = bundle.getString(HomeClassGoodsFragment.CIDFLAG).toString()
        }

    }

    override fun initData() {
        getPinpaiList()
    }

    private fun getPinpaiList() {
        EasyHttp.get(this)
            .api(ClassPinpaiListApi().apply {
                brandcat = cidStr
                min_id = pageIndex
            })
            .request(object :
                OnHttpListener<HttpData<ArrayList<ClassPinpaiListApi.PinpaiListDto>>> {
                override fun onSucceed(result: HttpData<ArrayList<ClassPinpaiListApi.PinpaiListDto>>?) {
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                    if (pageIndex == 1) {
                        classPinpaiListAdapter.clearData()
                    }
                    classPinpaiListAdapter.addData(result?.getData())
                }

                override fun onFail(e: Exception?) {
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                }

            })
    }
}