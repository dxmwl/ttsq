package com.easybuy.mobile.http.api

import com.hjq.http.annotation.HttpIgnore
import com.hjq.http.config.IRequestApi
import com.hjq.http.config.IRequestServer


/**
 * @project : EasyBuy_Android
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/6/23
 */
class GoodsListApi(private val urlPath: String) :IRequestApi, IRequestServer {

    override fun getHost(): String {
        return ""
    }

    @HttpIgnore
    val appkey: String? = null

    override fun getApi(): String {
        return urlPath
    }
}