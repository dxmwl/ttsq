package com.ttsq.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * 限时抢购
 */
class XsqgApi:IRequestApi {
    override fun getApi(): String {
        return "fastbuy"
    }

    var hour_type:Int = 1

    var min_id:Int = 1
}