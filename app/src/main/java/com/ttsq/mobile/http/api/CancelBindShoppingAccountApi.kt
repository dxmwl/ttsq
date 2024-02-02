package com.ttsq.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * @ClassName: CancelBindShoppingAccountApi
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/02 0002 15:03
 **/
class CancelBindShoppingAccountApi :IRequestApi{
    override fun getApi(): String {
        return "api/user/cancelBindShoppingAccount"
    }

    //0：淘宝
    var type:Int = 0
}