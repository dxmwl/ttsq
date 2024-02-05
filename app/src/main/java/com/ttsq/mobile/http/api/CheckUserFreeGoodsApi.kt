package com.ttsq.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * @ClassName: CheckUserFreeGoodsApi
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/05 0005 11:36
 **/
class CheckUserFreeGoodsApi : IRequestApi {
    override fun getApi(): String {
        return "api/goods/checkUserFreeGoods"
    }


    data class CheckUserFreeGoodsDto(
        val hasFreeGoods: Boolean
    )
}