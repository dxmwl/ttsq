package com.ttsq.mobile.http.api

import com.hjq.http.config.IRequestApi
import com.hjq.http.config.IRequestHost

/**
 * 淘口令识别
 */
class GetYouhuiApi : IRequestApi,IRequestHost {
    override fun getApi(): String {
        return "api/open_gaoyongzhuanlian_tkl.ashx"
    }

    var tkl = ""

    val signurl = 5

    val appkey = "2506f1398595428eab9055197bf671e7"
    val sid = "154994"
    val pid = "mm_111203980_10278226_34170938"

    data class GoodsYouhuiDto(
        val biaoqian: String,
        val category_id: String,
        val category_name: String,
        val code: String,
        val commentCount: String,
        val coupon_click_url: String,
        val coupon_end_time: String,
        val coupon_id: String,
        val coupon_info: String,
        val coupon_info_money: String,
        val coupon_remain_count: String,
        val coupon_start_time: String,
        val coupon_total_count: String,
        val creditLevel: String,
        val date_time: String,
        val date_time_yongjin: String,
        val favcount: String,
        val haitao: String,
        val item_url: String,
        val jianjie: String,
        val jinpaimaijia: String,
        val jiyoujia: String,
        val juhuasuan: String,
        val level_one_category_id: String,
        val level_one_category_name: String,
        val min_commission_rate: String,
        val nick: String,
        val pcDescContent: String,
        val pcDescContent_url: String,
        val pict_url: String,
        val pinpai: String,
        val pinpai_name: String,
        val presale_deposit: String,
        val presale_discount_fee_text: String,
        val presale_end_time: String,
        val presale_start_time: String,
        val presale_tail_end_time: String,
        val presale_tail_start_time: String,
        val provcity: String,
        val quanhou_jiage: String,
        val score1: String,
        val score2: String,
        val score3: String,
        val sellCount: String,
        val seller_id: String,
        val shopIcon: String,
        val shop_dsr: String,
        val shop_title: String,
        val shorturl: String,
        val shorturl2: String,
        val size: String,
        val small_images: String,
        val tag: String,
        val tao_id: String,
        val tao_title: String,
        val taobao_url: String,
        val taoqianggou: String,
        val title: String,
        val tkfee3: String,
        val tkl: String,
        val tkrate3: String,
        val type_one_id: String,
        val user_type: String,
        val volume: String,
        val white_image: String,
        val yongjin_type: String,
        val yunfeixian: String,
        val zhibo_url: String
    )

    override fun getHost(): String {
        return "https://api.zhetaoke.com:10001/"
    }
}