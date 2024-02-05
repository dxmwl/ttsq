package com.ttsq.mobile.ui.activity

import android.view.View
import com.hjq.base.BaseDialog
import com.hjq.http.EasyHttp
import com.hjq.http.listener.HttpCallback
import com.hjq.http.listener.OnHttpListener
import com.hjq.widget.layout.SettingBar
import com.ttsq.mobile.R
import com.ttsq.mobile.aop.SingleClick
import com.ttsq.mobile.app.AppActivity
import com.ttsq.mobile.http.api.CommitTxAccountApi
import com.ttsq.mobile.http.api.GetTxAccountApi
import com.ttsq.mobile.http.model.HttpData
import com.ttsq.mobile.ui.dialog.InputDialog
import com.ttsq.mobile.utils.livebus.LiveDataBus
import java.lang.Exception

/**
 * @ClassName: TxAccountActivity
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/04 0004 13:01
 **/
class TxAccountActivity : AppActivity() {

    private val input_zhifubao_account: SettingBar? by lazy { findViewById(R.id.input_zhifubao_account) }
    private val input_real_name: SettingBar? by lazy { findViewById(R.id.input_real_name) }
    override fun getLayoutId(): Int {
        return R.layout.activity_tx_account
    }

    override fun initView() {
        setOnClickListener(R.id.btn_commit, R.id.input_zhifubao_account, R.id.input_real_name)
    }

    override fun initData() {
        getTxAccount()
    }

    private fun getTxAccount() {
        EasyHttp.post(this)
            .api(GetTxAccountApi())
            .request(object : OnHttpListener<HttpData<GetTxAccountApi.TxAccountDto>> {
                override fun onSucceed(result: HttpData<GetTxAccountApi.TxAccountDto>?) {
                    result?.getData()?.let {
                        input_zhifubao_account?.setRightText(it.zhifubaoAccount)
                        input_real_name?.setRightText(it.realName)
                    }
                }

                override fun onFail(e: Exception?) {

                }

            })
    }

    @SingleClick
    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_commit -> {
                commitInfo()
            }

            R.id.input_zhifubao_account -> {
                InputDialog.Builder(this)
                    .setTitle("支付宝账号")
                    .setListener(object : InputDialog.OnListener {
                        override fun onConfirm(dialog: BaseDialog?, content: String) {
                            input_zhifubao_account?.setRightText(content)
                        }
                    })
                    .show()
            }

            R.id.input_real_name -> {
                InputDialog.Builder(this)
                    .setTitle("真实姓名")
                    .setListener(object : InputDialog.OnListener {
                        override fun onConfirm(dialog: BaseDialog?, content: String) {
                            input_real_name?.setRightText(content)
                        }
                    })
                    .show()
            }

            else -> {}
        }
    }

    private fun commitInfo() {
        val zhifubaoAccount = input_zhifubao_account?.getRightText()?.toString()
        if (zhifubaoAccount.isNullOrBlank()) {
            toast("请输入支付宝账号")
            return
        }
        val realName = input_real_name?.getRightText()?.toString()
        if (realName.isNullOrBlank()) {
            toast("请输入真实姓名")
            return
        }
        EasyHttp.post(this)
            .api(CommitTxAccountApi().apply {
                accountName = zhifubaoAccount
                this.realName = realName
            })
            .request(object : HttpCallback<HttpData<Any>>(this) {
                override fun onSucceed(result: HttpData<Any>?) {
                    toast("绑定成功")
                    LiveDataBus.postValue("changeTxAccount", "")
                }
            })
    }
}