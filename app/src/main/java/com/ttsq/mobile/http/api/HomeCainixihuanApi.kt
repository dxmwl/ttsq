package com.ttsq.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * 猜你喜欢
 */
class HomeCainixihuanApi : IRequestApi {
    override fun getApi(): String {
        return "api/open_item_guess_like2.ashx"
    }

    var page = 1

    var device_value: String = ""

//    var device_encrypt: String = "MD5"

    var device_type: String = "OAID"

//    data class CainixihuanGoodsInfo(
//        val is_default: String,
//        val request_id: String,
//        val result_list: ResultList
//    )
//
//    data class ResultList(
//        val map_data: ArrayList<HomeGoodsListApi.GoodsBean>
//    )
}