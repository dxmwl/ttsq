package com.ttsq.mobile.ui.activity

import android.view.View
import android.widget.TextView
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.ttsq.mobile.R
import com.ttsq.mobile.aop.SingleClick
import com.ttsq.mobile.app.AppActivity
import com.ttsq.mobile.http.api.GetWalletBalanceApi
import com.ttsq.mobile.http.api.UserInfoApi
import com.ttsq.mobile.http.model.HttpData
import com.ttsq.mobile.utils.livebus.LiveDataBus
import java.lang.Exception

/**
 * @ClassName: MyWalletActivity
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/04 0004 12:10
 **/
class MyWalletActivity: AppActivity() {

    private val tv_balance: TextView? by lazy { findViewById(R.id.tv_balance) }

    override fun getLayoutId(): Int {
        return R.layout.activity_my_wallet
    }

    override fun initView() {
        setOnClickListener(R.id.btn_tx)
    }

    override fun initData() {

        LiveDataBus.subscribe("updateWalletBalance", this) { data ->
            getWalletBalance()
        }

        getWalletBalance()
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