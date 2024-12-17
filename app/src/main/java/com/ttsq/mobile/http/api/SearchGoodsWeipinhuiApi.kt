package com.ttsq.mobile.http.api

/**
 * @project : EasyBuy_Android
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/6/23
 */
class SearchGoodsWeipinhuiApi : HaodankuV2BaseApi() {
    override fun getApi(): String {
        return "vip_goods_search"
    }

    //搜索关键字
    var keyword: String = ""

    var min_id: Int = 1

    var min_size: Int = 20
}