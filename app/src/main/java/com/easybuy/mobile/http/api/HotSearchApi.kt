package com.easybuy.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * 热搜
 */
class HotSearchApi : IRequestApi {
    override fun getApi(): String {
        return "api/api_guanjianci.ashx"
    }

    data class HotSeaDto(
        val keywords: String
    )
}