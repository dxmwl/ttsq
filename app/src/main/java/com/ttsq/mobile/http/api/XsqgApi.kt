package com.ttsq.mobile.http.api

/**
 * 限时抢购
 */
class XsqgApi : HaodankuBaseApi() {
    override fun getApi(): String {
        return "fastbuy"
    }

    var hour_type: Int = 1

    var min_id: Int = 1

}