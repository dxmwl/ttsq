package com.ttsq.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * @ClassName: BindShoppingAccountApi
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/02 0002 14:51
 **/
class BindShoppingAccountApi:IRequestApi {
    override fun getApi(): String {
        return "api/user/bindShoppingAccount"
    }

    //0：淘宝
    var type:Int = 0

    //授权token
    var accessToken:String? = ""

    data class BindShoppingAccountResult(
        val taobaoSpecialId:String,
        val taobaoAccountName:String
    )
}