package com.ttsq.mobile.http.api

/**
 * 朋友圈
 */
class PyqGoodsApi : HaodankuV2BaseApi() {
    override fun getApi(): String {
        return "selected_item"
    }

    var min_id: Int = 1

    data class PyqGoodsDto(
        val add_time: Long,
        val advise_time: String,
        val comment: String,
        val content: String,
        val copy_comment: String,
        val copy_content: String,
        val couponmoney: String,
        val couponurl: String,
        val dummy_click_statistics: String,
        val edit_id: String,
        val itemendprice: String,
        val itemid: String,
        val itempic: ArrayList<String>,
        val itemprice: String,
        val itemtitle: String,
        val show_comment: String,
        val show_content: String,
        val show_time: String,
        val sola_image: String,
        val title: String,
        val tkrates: String
    )
}