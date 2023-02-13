package com.ttsq.mobile.http.api

/**
 * 联想词
 */
class LianxiangApi : ZhetaokeBaseApi() {
    override fun getApi(): String {
        return "api/api_suggest.ashx"
    }

    var content: String = ""

    class LianxiangDto : ArrayList<String>()

}