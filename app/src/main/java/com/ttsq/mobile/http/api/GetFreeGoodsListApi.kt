package com.ttsq.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * @ClassName: GetFreeGoodsListApi
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/05 0005 11:42
 **/
class GetFreeGoodsListApi : IRequestApi {
    override fun getApi(): String {
        return "api/goods/getFreeGoodsList"
    }

    var pageNum = 1
    val pageSize = 20

    data class FreeGoodsDeo(
        val goodsId: String,
        val goodsImg: String,
        val goodsTitle: String,
        val goodsOriganPrice: String,
        val goodsZkPrice: String,
        val subsidyMoney: String,
        val couponsMoney: String,
        val tkRate: String
    )
}