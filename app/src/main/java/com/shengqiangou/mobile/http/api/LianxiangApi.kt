package com.shengqiangou.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * 联想词
 */
class LianxiangApi:IRequestApi {
    override fun getApi(): String {
        return "api/api_suggest.ashx"
    }

    var content:String = ""

    class LianxiangDto : ArrayList<String>()
}