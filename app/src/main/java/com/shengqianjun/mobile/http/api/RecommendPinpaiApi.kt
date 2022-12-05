package com.shengqianjun.mobile.http.api

import com.hjq.http.config.IRequestApi

class RecommendPinpaiApi : IRequestApi {
    override fun getApi(): String {
        return "brand_todayrecommend"
    }

    data class RecommendPinpaiDto(
        val `data`: Data,
        val items: ArrayList<Item>,
    )

    data class Data(
        val actitvity_endtime: String,
        val background: String,
        val brand_logo: String,
        val id: String,
        val multiple: List<Any>,
        val name: String,
        val num: Int,
        val title: String,
    )

    data class Item(
        val activity_plan: String,
        val activity_type: String,
        val activityid: String,
        val couponendtime: String,
        val couponinfo: String,
        val couponmoney: String,
        val couponnum: String,
        val couponreceive: String,
        val couponstarttime: String,
        val couponsurplus: String,
        val couponurl: String,
        val discount: String,
        val down_type: String,
        val general_index: String,
        val grade_avg: String,
        val id: String,
        val is_flagship_store: String,
        val is_foreshow: String,
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
        val rate_total: String,
        val report_status: String,
        val seller_id: String,
        val seller_name: String,
        val shopid: String,
        val shopname: String,
        val shoptype: String,
        val stock: String,
        val tkmoney: String,
        val tkrates: String,
        val todaysale: String,
        val userid: String,
        val videoid: String,
    )
}