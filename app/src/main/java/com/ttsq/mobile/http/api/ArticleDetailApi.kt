package com.ttsq.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * 文章详情
 */
class ArticleDetailApi : IRequestApi {
    override fun getApi(): String {
        return "talent_article"
    }

    var id: String = ""

    data class ArticleDetailDto(
        val addtime: Long,
        val app_image: String,
        val article: String,
        val article_banner: String,
        val autograph: Any,
        val checktime: String,
        val compose_image: String,
        val followtimes: String,
        val head_img: String,
        val highquality: String,
        val id: String,
        val itemid_str: String,
        val items: ArrayList<Item>,
        val label: String,
        val name: String,
        val readtimes: String,
        val shorttitle: String,
        val talent_id: String,
        val talent_name: String
    )

    data class Item(
        val couponexplain: String,
        val couponinfo: String,
        val couponmoney: String,
        val couponurl: String,
        val itemendprice: String,
        val itemid: String,
        val itempic: String,
        val itemprice: String,
        val itemsale: String,
        val itemshorttitle: String,
        val itemtitle: String,
        val min_buy: String,
        val shopname: String,
        val shoptype: String,
        val tkmoney: String,
        val tkrates: String,
        val xid: String
    )
}