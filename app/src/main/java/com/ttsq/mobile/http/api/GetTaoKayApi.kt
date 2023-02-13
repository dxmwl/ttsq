package com.ttsq.mobile.http.api

/**
 * 获取淘口令
 */
class GetTaoKayApi : HaodankuBaseApi() {
    override fun getApi(): String {
        return "api/open_tkl_create.ashx"
    }

    var url: String = ""
    var signurl = 0
    var type = 0
}