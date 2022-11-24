package com.shengqianjun.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * @project : EasyBuy_Android
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/6/23
 */
class SameClassGoodsApi : IRequestApi {
    override fun getApi(): String {
        return "api/open_item_guess_like.ashx"
    }

    //商品ID
    var item_id: String = ""
}