package com.shengqianjun.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * 全天实时榜单
 */
class QuantianBangdanApi:IRequestApi {
    override fun getApi(): String {
        return "api/api_quantian.ashx"
    }

    var sort:String = "new"
}