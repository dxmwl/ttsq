package com.ttsq.mobile.http.api

import com.hjq.http.config.IRequestApi

class GetPddItemsLinkApi: HaodankuV2BaseApi() {
    override fun getApi(): String {
        return "get_pdditems_link"
    }

    var goods_id: String = ""
    var goods_sign: String = ""

    data class GetPddItemsLinkApiDto(
        val mobile_url:String,
        val mobile_short_url:String,
        val url:String,
    )
}