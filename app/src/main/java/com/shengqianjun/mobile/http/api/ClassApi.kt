package com.shengqianjun.mobile.http.api

import com.shengqianjun.mobile.other.AppConfig
import com.hjq.http.config.IRequestApi
import com.hjq.http.config.IRequestHost

/**
 * 分类数据
 */
class ClassApi : IRequestApi ,IRequestHost{

    override fun getHost(): String {
        return AppConfig.getHakBaseUrl()
    }

    override fun getApi(): String {
        return "super_classify"
    }

    data class ClassInfo(
        val cid: Int,
        val main_name: String,
        var checked:Boolean = false,
        val `data`: ArrayList<Data>? = null
    )

    data class Data(
        val info: ArrayList<Info>,
        val next_name: String
    )

    data class Info(
        val imgurl: String,
        val son_name: String
    )
}