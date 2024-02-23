package com.ttsq.mobile.http.api

/**
 * @ClassName: GetDyGoodsListApi
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/22 0022 14:25
 **/
class GetDyGoodsListApi : HaodankuV3BaseApi() {
    override fun getApi(): String {
        return "dy_life_list"
    }

    var category_id: Int? = null
    var city_code: String? = null
    var longitude: String? = null
    var latitude: String? = null
    var min_id: Int? = null

    data class DyGoodsDto(
        val distance: String,
        val id: String,
        val item_img: String,
        val item_tag: List<ItemTag>?,
        val item_title: String,
        val origin_money: String,
        val origin_price: String,
        val origin_rates: Int,
        val price: String,
        val shop_name: String,
        val sold: String
    )

    data class ItemTag(
        val content: String
    )
}