package com.shengqiangou.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * 达人说
 */
class DarenshuoApi : IRequestApi {
    override fun getApi(): String {
        return "talent_info"
    }

    var talentcat: Int = 0

    data class DarenshuoListDto(
        val clickdata: List<Clickdata>,
        val newdata: ArrayList<Newdata>,
        val talent_Category: TalentCategory,
        val topdata: List<Topdata>
    )

    data class Clickdata(
        val app_image: String,
        val article: String,
        val article_banner: String,
        val compose_image: String,
        val followtimes: String,
        val head_img: String,
        val highquality: String,
        val id: String,
        val image: String,
        val itemnum: String,
        val label: String,
        val name: String,
        val readtimes: String,
        val shorttitle: String,
        val talent_id: String,
        val talent_name: String,
        val talentcat: String,
        val tk_item_id: String
    )

    data class Newdata(
        val app_image: Any,
        val article: String,
        val article_banner: String,
        val compose_image: String,
        val followtimes: String,
        val head_img: String,
        val highquality: String,
        val id: String,
        val image: String,
        val itemnum: String,
        val label: String,
        val name: String,
        val readtimes: String,
        val shorttitle: String,
        val talent_id: String,
        val talent_name: String,
        val talentcat: String,
        val tk_item_id: String
    )

    data class TalentCategory(
        val `1`: String,
        val `2`: String,
        val `3`: String,
        val `4`: String
    )

    data class Topdata(
        val app_image: String,
        val article: String,
        val article_banner: String,
        val compose_image: String,
        val followtimes: String,
        val highquality: String,
        val id: String,
        val image: String,
        val itemnum: String,
        val label: String,
        val name: String,
        val readtimes: String,
        val shorttitle: String,
        val talent_id: String,
        val talent_name: String,
        val talentcat: String,
        val tk_item_id: String
    )
}