package com.ttsq.mobile.http.api

import com.ttsq.mobile.manager.UserManager

/**
 * @project : EasyBuy_Android
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/6/23
 */
class GetLingquanUrlApi : HaodankuV2BaseApi() {
    override fun getApi(): String {
        return "ratesurl"
    }

    //商品ID,商品ID必须填
    var itemid: String = ""
    var get_taoword: String = "1"
    var title: String = ""

    val special_id = UserManager.userInfo?.taobaoSpecialId

    //	signurl=5，返回结果整合高佣转链API、解析商品编号API、全网商品详情API、淘口令创建API，已经自动判断和拼接使用全网G券还是全网S券。
    //signurl=4，返回结果整合高佣转链API、解析商品编号API、商品简版详情API、淘口令创建API，已经自动判断和拼接使用全网G券还是全网S券。
    //signurl=3，返回结果整合高佣转链API、解析商品编号API，已经自动判断和拼接使用全网G券还是全网S券。
    //signurl=0或1或2，返回官方高佣转链接口结果，需要自行判断和拼接使用全网G券或者全网S券。
    //signurl=0，表示直接返回最终结果。
    //signurl=1或2，表示返回淘宝联盟请求地址，大家拿到地址后再用自己的服务器二次请求即可获得最终结果，值为1返回http链接，值为2返回https安全链接。
//    var signurl = 5

    data class LingquanUrlDto(
        val coupon_click_url: String,
        val couponendtime: String,
        val couponexplain: String,
        val couponmoney: String,
        val couponnum: String,
        val couponreceive: String,
        val couponstarttime: String,
        val couponsurplus: String,
        val item_url: String,
        val max_commission_rate: String,
        val taoword: String,
        val title: String
    )
}