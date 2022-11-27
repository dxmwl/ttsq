package com.shengqianjun.mobile.ui.fragment

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.TimeUtils
import com.google.android.material.tabs.TabLayout
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.orhanobut.logger.Logger
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.shengqianjun.mobile.R
import com.shengqianjun.mobile.app.TitleBarFragment
import com.shengqianjun.mobile.http.api.XsqgApi
import com.shengqianjun.mobile.http.model.GoodsDetailDto
import com.shengqianjun.mobile.http.model.HourTypeDto
import com.shengqianjun.mobile.http.model.HttpData
import com.shengqianjun.mobile.ui.activity.HomeActivity
import com.shengqianjun.mobile.ui.adapter.XsqgGoodsAdapter
import java.util.*

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 友圈爆款 Fragment
 */
class YqbkFragment : TitleBarFragment<HomeActivity>(), TabLayout.OnTabSelectedListener {

    companion object {

        fun newInstance(): YqbkFragment {
            return YqbkFragment()
        }
    }

    private lateinit var xsqgGoodsAdapter: XsqgGoodsAdapter
    private var selectorPosition: Int = 1
    private val time_tab_layout: TabLayout? by lazy { findViewById(R.id.time_tab_layout) }
    private val refresh: SmartRefreshLayout? by lazy { findViewById(R.id.refresh) }
    private val goods_list: RecyclerView? by lazy { findViewById(R.id.goods_list) }

    private val hourType = ArrayList<HourTypeDto>()

    override fun getLayoutId(): Int {
        return R.layout.yqbk_fragment
    }

    override fun initView() {
        val buildHourType = buildHourType()
        initTimeTabList(buildHourType)

        goods_list?.let {
            it.layoutManager = LinearLayoutManager(requireContext())
            xsqgGoodsAdapter = XsqgGoodsAdapter(requireContext())
            it.adapter = xsqgGoodsAdapter
        }

        refresh?.setOnRefreshListener {
            pageIndex = 1
            val buildHourType2 = buildHourType()
            initTimeTabList(buildHourType2)
        }

        refresh?.setOnLoadMoreListener {
            pageIndex++
            getGoodsList()
        }
    }

    private fun buildHourType(): ArrayList<HourTypeDto> {
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        //今天0点时间戳
        val timeInMillis = calendar.timeInMillis
        Logger.d("时间$timeInMillis")
        //昨天0点
        hourType.add(
            HourTypeDto(
                timeInMillis - 1000 * 60 * 60 * 24,
                getStatusCurrent(timeInMillis - 1000 * 60 * 60 * 24),
                1
            )
        )
        //昨天10点
        hourType.add(
            HourTypeDto(
                timeInMillis - 1000 * 60 * 60 * 14,
                getStatusCurrent(timeInMillis - 1000 * 60 * 60 * 14),
                2
            )
        )
        //昨天12点
        hourType.add(
            HourTypeDto(
                timeInMillis - 1000 * 60 * 60 * 12,
                getStatusCurrent(timeInMillis - 1000 * 60 * 60 * 12),
                3
            )
        )
        //昨天15点
        hourType.add(
            HourTypeDto(
                timeInMillis - 1000 * 60 * 60 * 9,
                getStatusCurrent(timeInMillis - 1000 * 60 * 60 * 9),
                4
            )
        )
        //昨天20点
        hourType.add(
            HourTypeDto(
                timeInMillis - 1000 * 60 * 60 * 4,
                getStatusCurrent(timeInMillis - 1000 * 60 * 60 * 4),
                5
            )
        )
        //今天0点
        hourType.add(
            HourTypeDto(
                timeInMillis - 1000 * 60 * 60 * 0,
                getStatusCurrent(timeInMillis - 1000 * 60 * 60 * 0),
                6
            )
        )
        //今天10点
        hourType.add(
            HourTypeDto(
                timeInMillis + 1000 * 60 * 60 * 10,
                getStatusCurrent(timeInMillis + 1000 * 60 * 60 * 10),
                7
            )
        )
        //今天12点
        hourType.add(
            HourTypeDto(
                timeInMillis + 1000 * 60 * 60 * 12,
                getStatusCurrent(timeInMillis + 1000 * 60 * 60 * 12),
                8
            )
        )
        //今天15点
        hourType.add(
            HourTypeDto(
                timeInMillis + 1000 * 60 * 60 * 15,
                getStatusCurrent(timeInMillis + 1000 * 60 * 60 * 15),
                9
            )
        )
        //今天20点
        hourType.add(
            HourTypeDto(
                timeInMillis + 1000 * 60 * 60 * 20,
                getStatusCurrent(timeInMillis + 1000 * 60 * 60 * 20),
                10
            )
        )
        //明天0点
        hourType.add(
            HourTypeDto(
                timeInMillis + 1000 * 60 * 60 * 24,
                getStatusCurrent(timeInMillis + 1000 * 60 * 60 * 24),
                11
            )
        )
        //明天10点
        hourType.add(
            HourTypeDto(
                timeInMillis + 1000 * 60 * 60 * 34,
                getStatusCurrent(timeInMillis + 1000 * 60 * 60 * 34),
                12
            )
        )
        //明天12点
        hourType.add(
            HourTypeDto(
                timeInMillis + 1000 * 60 * 60 * 36,
                getStatusCurrent(timeInMillis + 1000 * 60 * 60 * 36),
                13
            )
        )
        //明天15点
        hourType.add(
            HourTypeDto(
                timeInMillis + 1000 * 60 * 60 * 37,
                getStatusCurrent(timeInMillis + 1000 * 60 * 60 * 37),
                14
            )
        )
        //明天20点
        hourType.add(
            HourTypeDto(
                timeInMillis + 1000 * 60 * 60 * 39,
                getStatusCurrent(timeInMillis + 1000 * 60 * 60 * 39),
                15
            )
        )

        return hourType
    }

    override fun initData() {

    }

    override fun isStatusBarEnabled(): Boolean {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled()
    }

    /**
     * 获取当前时间状态
     */
    private fun getStatusCurrent(timeInMillis: Long): String {
        //超过当前时间和已过去4小时为已结束货即将开抢
        val currentTimeMillis = System.currentTimeMillis()
        if (timeInMillis > currentTimeMillis) {
            return "即将开抢"
        }
        return if ((currentTimeMillis - timeInMillis) > 1000 * 60 * 60 * 4) {
            "已开抢"
        } else {
            "正在疯抢"
        }
    }

    /**
     * 构造时间列表
     */
    private fun initTimeTabList(data: List<HourTypeDto>) {
        selectorPosition = data.size
        data.forEachIndexed { index, xsqgTimeDto ->
            val newTab = time_tab_layout?.newTab()
            val inflate = View.inflate(requireContext(), R.layout.item_xsqg_tab, null)
            inflate.findViewById<TextView>(R.id.tab_time)?.text =
                TimeUtils.millis2String(xsqgTimeDto.timeInMillis, "hh:mm")
            inflate.findViewById<TextView>(R.id.tab_state)?.text = xsqgTimeDto.status
            newTab?.customView = inflate
            if (xsqgTimeDto.status == "正在疯抢") {
                selectorPosition = index
            }
            if (newTab != null) {
                time_tab_layout?.addTab(newTab)
            }
        }
        time_tab_layout?.addOnTabSelectedListener(this)
        time_tab_layout?.postDelayed({ time_tab_layout?.getTabAt(selectorPosition)?.select() }, 500)

        getGoodsList()
    }

    private var pageIndex = 1

    private fun getGoodsList() {
        val hourTypeDto = hourType[selectorPosition]
        EasyHttp.get(this)
            .api(XsqgApi().apply {
                hour_type = hourTypeDto.type
                min_id = pageIndex
            })
            .request(object : OnHttpListener<HttpData<ArrayList<GoodsDetailDto>>> {
                override fun onSucceed(result: HttpData<ArrayList<GoodsDetailDto>>?) {
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                    result?.getData()?.let {
                        if (pageIndex==1) {
                            xsqgGoodsAdapter.setData(it)
                        }else{
                            xsqgGoodsAdapter.addData(it)
                        }
                    }
                }

                override fun onFail(e: Exception?) {
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                    toast(e?.message)
                }
            })
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        selectorPosition = tab?.position!!
        pageIndex = 1
        getGoodsList()
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }
}