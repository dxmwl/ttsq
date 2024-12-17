package com.ttsq.mobile.http.api

import com.hjq.http.config.IRequestApi

class GetVipRatesurlApi : HaodankuV2BaseApi() {
    override fun getApi(): String {
        return "vip_ratesurl"
    }

    var goodsid: String = ""

    data class GetVipRatesurlApiDto(
        val url: String
    )
}