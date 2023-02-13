package com.ttsq.mobile.http.api

/**
 * 全天实时榜单
 */
class QuantianBangdanApi : HaodankuBaseApi() {
    override fun getApi(): String {
        return "api/api_quantian.ashx"
    }

    var sort: String = "new"
}