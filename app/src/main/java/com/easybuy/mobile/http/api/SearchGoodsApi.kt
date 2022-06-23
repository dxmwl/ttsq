package com.easybuy.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * @project : EasyBuy_Android
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/6/23
 */
class SearchGoodsApi : IRequestApi {
    override fun getApi(): String {
        return "api/api_quanwang.ashx"
    }

    //搜索关键字
    var q: String = ""

    var page: Int = 1
    var page_size: Int = 20

}