package com.ttsq.mobile.http.api

/**
 * @project : EasyBuy_Android
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/6/23
 */
class SameClassGoodsApi : HaodankuV2BaseApi() {
    override fun getApi(): String {
        return "get_similar_info"
    }

    //商品ID
    var itemid: String = ""
}