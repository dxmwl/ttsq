package com.ttsq.mobile.ui.activity

import android.view.View
import com.ali.auth.third.core.MemberSDK
import com.ali.auth.third.core.callback.LoginCallback
import com.ali.auth.third.core.model.Session
import com.ali.auth.third.login.LoginService
import com.ali.auth.third.ui.context.CallbackContext.activity
import com.blankj.utilcode.util.GsonUtils
import com.orhanobut.logger.Logger
import com.ttsq.mobile.R
import com.ttsq.mobile.aop.SingleClick
import com.ttsq.mobile.app.AppActivity


/**
 * @ClassName: AuthorizationManagementActivity
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/01 0001 15:10
 **/
class AuthorizationManagementActivity: AppActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_authorization_management
    }

    override fun initView() {
        setOnClickListener(R.id.type_taobao)
    }

    override fun initData() {
        
    }

    @SingleClick
    override fun onClick(view: View) {
        when (view.id) {
            R.id.type_taobao -> {
                taobaoAuthorization()
            }
            else -> {}
        }
    }

    private fun taobaoAuthorization() {
        val loginService = MemberSDK.getService(LoginService::class.java)
        loginService?.auth(this, object : LoginCallback {
            override fun onSuccess(session: Session?) {
                Logger.d("授权成功:${GsonUtils.toJson(session)}")
            }
            override fun onFailure(code: Int, msg: String) {
                toast("授权失败：${code} ${msg}")
                Logger.e("授权失败：${code} ${msg}")
            }
        })
    }

}