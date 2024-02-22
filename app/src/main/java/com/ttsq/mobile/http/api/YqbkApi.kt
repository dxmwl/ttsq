package com.ttsq.mobile.http.api

/**
 * 友圈爆款
 */
class YqbkApi : HaodankuV2BaseApi() {
    override fun getApi(): String {
        return "api/api_all.ashx"
    }

    /**
     * 	商品排序方式，new：按照综合排序，
    total_sale_num_asc：按照总销量从小到大排序，total_sale_num_desc：按照总销量从大到小排序，
    sale_num_asc：按照月销量从小到大排序，sale_num_desc：按照月销量从大到小排序，
    commission_rate_asc：按照佣金比例从小到大排序，commission_rate_desc：按照佣金比例从大到小排序，
    price_asc：按照价格从小到大排序，price_desc：按照价格从大到小排序，
    coupon_info_money_asc：按照优惠券金额从小到大排序，coupon_info_money_desc：按照优惠券金额从大到小排序，
    shop_level_asc：按照店铺等级从低到高排序，shop_level_desc：按照店铺等级从高到低排序，
    tkfee_asc：按照返佣金额从低到高排序，tkfee_desc：按照返佣金额从高到低排序，
    code：按照code值从大到小排序，date_time：按照更新时间排序，random：按照随机排序
     */
    val sort = "new"

    var page = 1

    var page_size = 20

    /**
     * 一级商品分类，值为空：全部商品，1：女装，2：母婴，3：美妆，4：居家日用，5：鞋品，6：美食，7：文娱车品，8：数码家电，9：男装，10：内衣，11：箱包，12：配饰，13：户外运动，14：家装家纺
     */
    var cid = ""

    //	是否朋友圈火爆商品，值为空：全部商品，1：朋友圈火爆商品
    var tag = ""

    data class YqbkGoodsInfo(
        val biaoqian: String,
        val category_id: String,
        val category_name: String,
        val code: String,
        val itemid: String,
        val commentCount: String,
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
        val nick: String,
        val pcDescContent: String,
        val pict_url: String,
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
        val size: String,
        val small_images: String,
        val tag: String,
        val tao_id: String,
        val tao_title: String,
        val taoqianggou: String,
        val title: String,
        val tkfee3: String,
        val tkrate3: String,
        val type_one_id: String,
        val user_type: String,
        val volume: String,
        val white_image: String,
        val yongjin_type: String,
        val yunfeixian: String,
        val zhibo_url: String
    )
}