package com.ttsq.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2019/12/07
 *    desc   : 验证码校验
 */
class VerifyCodeApi : IRequestApi {

    override fun getApi(): String {
        return "api/user/verifyUserIdentity"
    }

    /** 手机号 */
    private var phoneNumber: String? = null

    /** 验证码 */
    private var phoneCode: String? = null

    fun setPhone(phoneNumber: String?): VerifyCodeApi = apply {
        this.phoneNumber = phoneNumber
    }

    fun setCode(phoneCode: String?): VerifyCodeApi = apply {
        this.phoneCode = phoneCode
    }
}