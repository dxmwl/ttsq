package com.ttsq.mobile.ui.activity

import android.text.InputFilter
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.hjq.http.EasyHttp
import com.hjq.http.listener.HttpCallback
import com.hjq.http.listener.OnHttpListener
import com.ttsq.mobile.R
import com.ttsq.mobile.aop.SingleClick
import com.ttsq.mobile.app.AppActivity
import com.ttsq.mobile.http.api.CommitTxApi
import com.ttsq.mobile.http.api.GetTxAccountApi
import com.ttsq.mobile.http.api.GetWalletBalanceApi
import com.ttsq.mobile.http.model.HttpData
import com.ttsq.mobile.other.MoneyInputFilter
import com.ttsq.mobile.utils.livebus.LiveDataBus
import java.math.BigDecimal


/**
 * @ClassName: TxActivity
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/04 0004 13:10
 **/
class TxActivity : AppActivity() {

    private var inputMoney: Double = 0.0
    private val account_number: TextView? by lazy { findViewById(R.id.account_number) }
    private val real_name: TextView? by lazy { findViewById(R.id.real_name) }
    private val tv_balance: TextView? by lazy { findViewById(R.id.tv_balance) }
    private val input_money: EditText? by lazy { findViewById(R.id.input_money) }

    override fun getLayoutId(): Int {
        return R.layout.activity_tx
    }

    override fun initView() {
        setOnClickListener(R.id.change_zhifubao, R.id.btn_all, R.id.btn_commit)

        // 创建一个InputFilter数组，可以添加多个过滤器
        val filters = arrayOfNulls<InputFilter>(1)
        filters[0] = MoneyInputFilter()

        // 将过滤器应用到EditText上
        input_money?.filters = filters

        input_money?.addTextChangedListener {
            inputMoney = try {
                BigDecimal(it.toString()).toDouble()
            } catch (e: Exception) {
                BigDecimal("0").toDouble()
            }
        }
    }

    override fun initData() {
        LiveDataBus.subscribe("changeTxAccount", this) { data ->
            getTxAccount()
        }
        LiveDataBus.subscribe("updateWalletBalance", this) { data ->
            getWalletBalance()
        }

        getWalletBalance()
        getTxAccount()
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

    private fun getTxAccount() {
        EasyHttp.post(this)
            .api(GetTxAccountApi())
            .request(object : OnHttpListener<HttpData<GetTxAccountApi.TxAccountDto>> {
                override fun onSucceed(result: HttpData<GetTxAccountApi.TxAccountDto>?) {
                    result?.getData()?.let {
                        account_number?.text = it.zhifubaoAccount
                        real_name?.text = it.realName
                    }
                }

                override fun onFail(e: Exception?) {

                }

            })
    }

    @SingleClick
    override fun onClick(view: View) {
        when (view.id) {
            R.id.change_zhifubao -> {
                startActivity(TxAccountActivity::class.java)
            }

            R.id.btn_all -> {
                input_money?.setText(tv_balance?.text)
            }

            R.id.btn_commit -> {
                commitInfo()
            }

            else -> {}
        }
    }

    private fun commitInfo() {
        if (inputMoney < 1) {
            toast("请输入大于1的金额")
            return
        }
        EasyHttp.post(this)
            .api(CommitTxApi().apply {
                this.txMoney = inputMoney.toString()
            })
            .request(object : HttpCallback<HttpData<Any>>(this) {
                override fun onSucceed(result: HttpData<Any>?) {
                    toast("申请已提交，请等待审核")
                    LiveDataBus.postValue("upDateUserInfo", "")
                }
            })
    }
}