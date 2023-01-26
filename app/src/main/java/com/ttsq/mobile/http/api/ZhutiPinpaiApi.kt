package com.ttsq.mobile.http.api

import com.hjq.http.config.IRequestApi

class ZhutiPinpaiApi : IRequestApi {
    override fun getApi(): String {
        return "brand_subjectitems"
    }

    data class ZhutiPinpaiDto(
        val brand_id: String,
        val brand_image: String,
        val brand_logo: String,
        val brand_name: String,
        val id: String,
        val items: ArrayList<RecommendPinpaiApi.Item>,
    )

}