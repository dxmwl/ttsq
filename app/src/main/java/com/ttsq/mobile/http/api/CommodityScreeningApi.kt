package com.ttsq.mobile.http.api

/**
 * 商品筛选
 */
class CommodityScreeningApi : HaodankuV2BaseApi()  {
    override fun getApi(): String {
        return "column"
    }

    var cid: String = ""
    var sort: String = ""
    var min_id: Int = 1

    data class ShaixuanGoodsDto(
        val activity_type: String,
        val activityid: String,
        val coupon_condition: String,
        val couponendtime: String,
        val couponexplain: String,
        val couponinfo: String,
        val couponmoney: String,
        val couponnum: String,
        val couponreceive: String,
        val couponreceive2: String,
        val couponstarttime: String,
        val couponsurplus: String,
        val couponurl: String,
        val cuntao: String,
        val deposit: String,
        val deposit_deduct: String,
        val discount: String,
        val down_type: String,
        val end_time: String,
        val fqcat: String,
        val general_index: String,
        val guide_article: String,
        val is_brand: String,
        val is_explosion: String,
        val is_live: String,
        val is_shipping: String,
        val isquality: String,
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
        val me: String,
        val min_buy: String,
        val online_users: String,
        val original_article: String,
        val original_img: String,
        val planlink: String,
        val presale_discount_fee_text: String,
        val presale_end_time: Int,
        val presale_start_time: Int,
        val presale_tail_end_time: Int,
        val presale_tail_start_time: Int,
        val product_id: String,
        val report_status: String,
        val seller_id: String,
        val seller_name: String,
        val sellernick: String,
        val shopid: String,
        val shopname: String,
        val shoptype: String,
        val son_category: String,
        val start_time: String,
        val starttime: String,
        val taobao_image: String,
        val tkmoney: String,
        val tkrates: String,
        val tktype: String,
        val todaycouponreceive: String,
        val todaysale: String,
        val userid: String,
        val videoid: String,
        val xid: String
    )
}