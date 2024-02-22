package com.ttsq.mobile.http.api

/**
 * 获取淘口令
 */
class GetTaoKayApi : HaodankuV2BaseApi() {
    override fun getApi(): String {
        return "api/open_tkl_create.ashx"
    }

    var url: String = ""
    var signurl = 0
    var type = 0
}