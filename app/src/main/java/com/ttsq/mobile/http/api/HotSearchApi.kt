package com.ttsq.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * 热搜
 */
class HotSearchApi : IRequestApi {
    override fun getApi(): String {
        return "hot_key"
    }

    data class HotSeaDto(
        val keyword: String
    )
}