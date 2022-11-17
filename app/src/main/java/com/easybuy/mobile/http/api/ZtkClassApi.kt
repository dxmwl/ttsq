package com.easybuy.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * 折淘客分类
 */
class ZtkClassApi : IRequestApi {
    override fun getApi(): String {
        return "api/api_type.ashx"
    }

    data class ClassInfo(
        val cid: String,
        val name: String,
        val q: String = "",
        val q_pic: String = "",
        var checked: Boolean = false,
        var childs: ArrayList<ClassInfo> = ArrayList()
    )

}