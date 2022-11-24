package com.shengqianjun.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * 实时榜单
 */
class ShishiBangdanApi:IRequestApi {
    override fun getApi(): String {
        return "api/api_xiaoshi.ashx"
    }

    var sort:String = "new"
}