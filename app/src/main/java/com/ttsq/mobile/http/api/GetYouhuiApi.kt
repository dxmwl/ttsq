package com.ttsq.mobile.http.api

import com.hjq.http.config.IRequestApi
import com.hjq.http.config.IRequestHost
import com.ttsq.mobile.manager.UserManager

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
        val couponLink: String,
        //平台类型(1淘宝,2:京东,3:抖音)
        val platformType: Int
    )
}