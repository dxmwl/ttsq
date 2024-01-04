package com.ttsq.mobile.http.api

import com.hjq.http.config.IRequestApi
import com.hjq.http.config.IRequestHost

/**
 * 淘口令识别
 */
class GetYouhuiApi : IRequestApi {

    override fun getApi(): String {
        return "api/goods/searchYouhui"
    }

    var content = ""

    data class GoodsYouhuiDto(
        val title: String,
        val goodsImg: String,
        val saleNum: String,
        val originPrice: String,
        val currentPrice: String,
        val couponsPrice: String,
        val couponLink: String
        )
}