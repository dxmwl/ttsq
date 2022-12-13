package com.shengqiangou.mobile.http.api

import com.hjq.http.config.IRequestApi


/**
 * @project : EasyBuy_Android
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/6/23
 */
class GoodsListApi() : IRequestApi {

    override fun getApi(): String {
        return "get_index_activity_items"
    }

    var id: String = ""

    data class HuodongGoodsDto(
        val block: ArrayList<Block>
    )

    data class Block(
        val item: ArrayList<CommodityScreeningApi.ShaixuanGoodsDto>,
        val name: String
    )

//    data class Item(
//        val activity_type: String,
//        val activityid: String,
//        val collect: Int,
//        val coupon_condition: Int,
//        val couponendtime: Int,
//        val couponexplain: String,
//        val couponmoney: Int,
//        val couponnum: Int,
//        val couponreceive: Int,
//        val couponreceive2: Int,
//        val couponstarttime: Int,
//        val couponsurplus: Int,
//        val couponurl: String,
//        val cuntao: Int,
//        val deposit: Int,
//        val deposit_deduct: Int,
//        val discount: Double,
//        val down_type: Int,
//        val end_time: Int,
//        val fqcat: Int,
//        val general_index: Int,
//        val gift_price: String,
//        val guide_article: String,
//        val is_brand: Int,
//        val is_explosion: Int,
//        val is_live: Int,
//        val is_shipping: Int,
//        val isquality: String,
//        val itemdesc: String,
//        val itemendprice: Double,
//        val itemid: Long,
//        val itempic: String,
//        val itempic_copy: String,
//        val itemprice: Double,
//        val itemsale: Int,
//        val itemsale2: Int,
//        val itemshorttitle: String,
//        val itemtitle: String,
//        val me: String,
//        val min_buy: Int,
//        val online_users: Int,
//        val original_article: String,
//        val original_img: String,
//        val planlink: String,
//        val product_id: Int,
//        val report_status: Int,
//        val seller_id: Int,
//        val seller_name: String,
//        val sellernick: String,
//        val shopid: Int,
//        val shopname: String,
//        val shoptype: String,
//        val son_category: Int,
//        val start_time: Int,
//        val starttime: Int,
//        val taobao_image: String,
//        val tkmoney: Double,
//        val tkrates: Double,
//        val tktype: String,
//        val todaycouponreceive: Int,
//        val todaysale: Int,
//        val userid: Long,
//        val videoid: Long,
//        val xid: String
//    )
}