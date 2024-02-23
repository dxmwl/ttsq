package com.ttsq.mobile.http.api

/**
 * 榜单
 */
class BangdanGoodsApi : HaodankuV2BaseApi() {
    override fun getApi(): String {
        return "sales_list"
    }

    /**
     * 	榜单类型：sale_type=1是实时销量榜（近2小时销量），type=2是今日爆单榜，type=3是昨日爆单榜，type=4是出单指数榜
     */
    var sale_type: Int = 1

    var min_id: Int = 1

    var cid: String = "0"
}