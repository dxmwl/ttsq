package com.ttsq.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * @ClassName: OneClickLoginApi
 * @Description: 手机号一键登录
 * @Author: 常利兵
 * @Date: 2023/3/6 16:15
 **/
class OneClickLoginApi:IRequestApi {
    override fun getApi(): String {
        return "api/user/oneClickLogin"
    }

    var umengToken = ""
}