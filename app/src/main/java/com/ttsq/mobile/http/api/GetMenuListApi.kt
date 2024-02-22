package com.ttsq.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * @ClassName: GetMenuListApi
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/22 0022 11:15
 **/
class GetMenuListApi : IRequestApi {
    override fun getApi(): String {
        return "api/bdsh/getAppMenuList"
    }

    /**
     * （0：本地生活，1：首页）
     */
    var type: Int? = null


    data class AppMenuDto(
        val menuTitle: String,
        val menuIcon: String,
        val menuLabel: String?,
        //1：链接，2：小程序，3：app，4：app页面，5：本地生活
        val type: Int,
        val paramsValue: String,
    )
}