package com.shengqianjun.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * 好单库分类
 */
class HdkClassApi : IRequestApi {
    override fun getApi(): String {
        return "super_classify"
    }

    data class ClassInfo(
        val cid: String,
        val `data`: ArrayList<Data>,
        val main_name: String,
        var checked: Boolean = false
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