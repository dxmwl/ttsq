package com.ttsq.mobile.http.api

/**
 * 热搜
 */
class HotSearchApi : HaodankuV2BaseApi()  {
    override fun getApi(): String {
        return "hot_key"
    }

    data class HotSeaDto(
        val keyword: String
    )
}