package com.shengqiangou.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * 品牌详情
 */
class PinpaiDetailApi : IRequestApi {
    override fun getApi(): String {
        return "singlebrand"
    }

    var id: String = ""
    var back = 20
    var min_id = 20
    //自定义排序【1.最新，2.销量（高到低），3.销量（低到高），4.价格(低到高)，5.价格（高到低），6.佣金比例（高到低）】
    var order = 2

    data class PinpaiDetailDto(
    val brand_compile: String,
    val brand_logo: String,
    val brand_plain: String,
    val brand_recommend: String,
    val brandcat: String,
    val fans: String,
    val fq_brand_name: String,
    val id: String,
    val inside_logo: String,
    val introduce: String,
    val items: ArrayList<Item>,
    val tb_brand_name: String
)

data class Item(
    val activity_type: String,
    val brand_name: String,
    val couponendtime: String,
    val couponexplain: String,
    val couponinfo: String,
    val couponmoney: String,
    val couponstarttime: String,
    val couponurl: String,
    val deposit: String,
    val deposit_deduct: String,
    val end_time: String,
    val general_index: String,
    val id: String,
    val itemdesc: String,
    val itemendprice: String,
    val itemid: String,
    val itempic: String,
    val itempic_copy: String,
    val itemprice: String,
    val itemsale: String,
    val itemsale2: String,
    val itemshorttitle: String,
    val itemtitle: String,
    val min_buy: String,
    val sellernick: String,
    val shop_score: ShopScore,
    val shopicon: String,
    val shopid: String,
    val shopname: String,
    val shoptype: String,
    val son_category: String,
    val start_time: String,
    val tkmoney: String,
    val tkrates: String,
    val tktype: String,
    val todaysale: String,
    val userid: String,
    val xid: String
)

data class ShopScore(
    val desc_level: Int,
    val desc_score: Double,
    val post_level: Int,
    val post_score: Double,
    val serv_level: Int,
    val serv_score: Double
)
}