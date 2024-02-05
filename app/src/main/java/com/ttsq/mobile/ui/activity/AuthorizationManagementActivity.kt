package com.ttsq.mobile.ui.activity

import android.view.View
import android.widget.Toast
import com.ali.auth.third.core.MemberSDK
import com.ali.auth.third.core.callback.LoginCallback
import com.ali.auth.third.core.model.Session
import com.ali.auth.third.core.model.User
import com.ali.auth.third.login.LoginService
import com.blankj.utilcode.util.GsonUtils
import com.hjq.base.BaseDialog
import com.hjq.http.EasyHttp
import com.hjq.http.listener.HttpCallback
import com.hjq.widget.layout.SettingBar
import com.orhanobut.logger.Logger
import com.randy.alibcextend.auth.AuthCallback
import com.randy.alibcextend.auth.TopAuth
import com.ttsq.mobile.R
import com.ttsq.mobile.aop.SingleClick
import com.ttsq.mobile.app.AppActivity
import com.ttsq.mobile.http.api.BindShoppingAccountApi
import com.ttsq.mobile.http.api.CancelBindShoppingAccountApi
import com.ttsq.mobile.http.model.HttpData
import com.ttsq.mobile.manager.UserManager
import com.ttsq.mobile.ui.dialog.MessageDialog
import com.ttsq.mobile.utils.livebus.LiveDataBus


/**
 * @ClassName: AuthorizationManagementActivity
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/01 0001 15:10
 **/
class AuthorizationManagementActivity : AppActivity() {

    private val type_taobao: SettingBar? by lazy { findViewById(R.id.type_taobao) }

    override fun getLayoutId(): Int {
        return R.layout.activity_authorization_management
    }

    override fun initView() {
        setOnClickListener(R.id.type_taobao)
    }

    override fun initData() {
        UserManager.userInfo?.let {
            if (it.taobaoSpecialId.isNullOrBlank().not()) {
                type_taobao?.setRightText("已绑定")
            }
        }
    }

    @SingleClick
    override fun onClick(view: View) {
        when (view.id) {
            R.id.type_taobao -> {
                UserManager.userInfo?.let {
                    if (it.taobaoSpecialId.isNullOrBlank().not()) {
                        MessageDialog.Builder(this)
                            .setTitle("取消绑定")
                            .setMessage("取消绑定淘宝账号，将无法获取淘宝返利福利")
                            .setConfirm("再想想")
                            .setCancel("取消绑定")
                            .setListener(object : MessageDialog.OnListener {
                                override fun onConfirm(dialog: BaseDialog?) {
                                    dialog?.dismiss()
                                }

                                override fun onCancel(dialog: BaseDialog?) {
                                    cancelShoppingAccount(0)
                                }
                            })
                            .show()
                    } else {
                        taobaoAuthorization()
                    }
                }
            }

            else -> {}
        }
    }

    /**
     * 取消授权购物账号
     * @param type 0:淘宝
     */
    private fun cancelShoppingAccount(type: Int) {
        EasyHttp.post(this)
            .api(CancelBindShoppingAccountApi().apply {
                this.type = type
            })
            .request(object : HttpCallback<HttpData<Any>>(this) {
                override fun onSucceed(result: HttpData<Any>?) {
                    toast("取消绑定成功")
                    when (type) {
                        0 -> {
                            UserManager.userInfo?.let {
                                it.taobaoSpecialId = null
                                it.taobaoAccountName = null
                            }
                            type_taobao?.setRightText("")
                        }

                        else -> {}
                    }
                }
            })
    }

    private fun taobaoAuthorization() {
        TopAuth.showAuthDialog(
            this@AuthorizationManagementActivity,
            R.mipmap.ic_launcher_round,
            "天天省钱",
            "34380301",
            object : AuthCallback {
                override fun onSuccess(accessToken: String, expireTime: String) {
                    Logger.d("授权回调成功:${accessToken} ${expireTime}")
                    bindShoppingAccount(0, accessToken)
                }

                override fun onError(code: String, msg: String) {
                    Logger.e("授权失败：$code $msg")
                    toast(msg)
                }
            })
    }

    /**
     * 绑定购物账号
     */
    private fun bindShoppingAccount(type: Int, accessToken: String) {
        EasyHttp.post(this)
            .api(BindShoppingAccountApi().apply {
                this.type = type
                this.accessToken = accessToken
            })
            .request(object :
                HttpCallback<HttpData<BindShoppingAccountApi.BindShoppingAccountResult>>(this) {
                override fun onSucceed(result: HttpData<BindShoppingAccountApi.BindShoppingAccountResult>?) {
                    toast("绑定成功")
                    when (type) {
                        0 -> {
                            UserManager.userInfo?.let {
                                it.taobaoSpecialId = result?.getData()?.taobaoSpecialId
                                it.taobaoAccountName = result?.getData()?.taobaoAccountName
                            }
                        }

                        else -> {}
                    }
                    type_taobao?.setRightText("已绑定")
                }
            })
    }

}