package com.ttsq.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * @ClassName: GetCommonConfigApi
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/04 0004 11:35
 **/
class GetCommonConfigApi:IRequestApi {
    override fun getApi(): String {
        return "api/common/getConfig"
    }

    data class AppConfigDto(
        val rebateRate:String
    )
}