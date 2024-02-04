package com.ttsq.mobile.ui.activity

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hjq.http.EasyHttp
import com.hjq.http.listener.HttpCallback
import com.hjq.http.listener.OnHttpListener
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.ttsq.mobile.R
import com.ttsq.mobile.aop.SingleClick
import com.ttsq.mobile.app.AppActivity
import com.ttsq.mobile.http.api.GetWalletBalanceApi
import com.ttsq.mobile.http.api.GetWalletBalanceChangeLogApi
import com.ttsq.mobile.http.api.UserInfoApi
import com.ttsq.mobile.http.model.HttpData
import com.ttsq.mobile.ui.adapter.WalletBlanceChangeLogAdapter
import com.ttsq.mobile.utils.livebus.LiveDataBus
import java.lang.Exception

/**
 * @ClassName: MyWalletActivity
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/04 0004 12:10
 **/
class MyWalletActivity : AppActivity() {

    private lateinit var walletBlanceChangeLogAdapter: WalletBlanceChangeLogAdapter
    private val tv_balance: TextView? by lazy { findViewById(R.id.tv_balance) }
    private val refresh: SmartRefreshLayout? by lazy { findViewById(R.id.refresh) }
    private val income_list: RecyclerView? by lazy { findViewById(R.id.income_list) }
    private var pageNum = 1

    override fun getLayoutId(): Int {
        return R.layout.activity_my_wallet
    }

    override fun initView() {
        setOnClickListener(R.id.btn_tx)

        income_list?.also {
            it.layoutManager = LinearLayoutManager(this)
            walletBlanceChangeLogAdapter = WalletBlanceChangeLogAdapter(this@MyWalletActivity)
            it.adapter = walletBlanceChangeLogAdapter
        }

        refresh?.setOnRefreshListener {
            pageNum = 1
            getWalletBalanceChangeLog()
        }
        refresh?.setOnLoadMoreListener {
            pageNum++
            getWalletBalanceChangeLog()
        }
    }

    override fun initData() {

        LiveDataBus.subscribe("updateWalletBalance", this) { data ->
            getWalletBalance()
        }

        getWalletBalance()

        getWalletBalanceChangeLog()
    }

    private fun getWalletBalanceChangeLog() {
        EasyHttp.post(this)
            .api(GetWalletBalanceChangeLogApi().apply {
                this.pageNum = this@MyWalletActivity.pageNum
            })
            .request(object :
                OnHttpListener<HttpData<ArrayList<GetWalletBalanceChangeLogApi.WalletBalanceDto>>> {
                override fun onSucceed(result: HttpData<ArrayList<GetWalletBalanceChangeLogApi.WalletBalanceDto>>?) {
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                    result?.getData()?.let {
                        if (pageNum == 1) {
                            walletBlanceChangeLogAdapter.clearData()
                        }
                        walletBlanceChangeLogAdapter.addData(it)
                    }
                }

                override fun onFail(e: Exception?) {
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                    toast(e?.message)
                }

            })
    }

    private fun getWalletBalance() {
        EasyHttp.post(this)
            .api(GetWalletBalanceApi())
            .request(object : OnHttpListener<HttpData<GetWalletBalanceApi.WallerBalanceDto>> {
                override fun onSucceed(result: HttpData<GetWalletBalanceApi.WallerBalanceDto>?) {
                    result?.getData()?.let {
                        tv_balance?.text = it.balance
                    }
                }

                override fun onFail(e: Exception?) {

                }
            })
    }


    override fun onRightClick(view: View) {
        startActivity(TxAccountActivity::class.java)
    }

    @SingleClick
    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_tx -> {
                startActivity(TxActivity::class.java)
            }

            else -> {}
        }
    }
}