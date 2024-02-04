package com.ttsq.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * @ClassName: GetShoppingTaobaoOrderListApi
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/04 0004 17:46
 **/
class GetShoppingTaobaoOrderListApi : IRequestApi {
    override fun getApi(): String {
        return "api/order/getUserShoppingOrder"
    }

    var pageNum = 1
    val pageSize = 20
    var status: Int = 0

    data class TaobaoShoppingOrderDto(
        val id: String,
        val itemId: String,
        val itemImg: String,
        val itemTitle: String,
        val alipayTotalPrice: String,
        val rebateRate: String,
        val createTime: String,
    )
}