package com.ttsq.mobile.http.api

/**
 * @project : EasyBuy_Android
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/6/23
 */
class SearchGoodsApi : HaodankuBaseApi() {
    override fun getApi(): String {
        return "supersearch"
    }

    //搜索关键字
    var keyword: String = ""

    var min_id: Int = 1

    var back: Int = 20

    //	是否有券，1为有券，其它值为全部商品
    var is_coupon: Int = 1

    /**
     * 	0.综合，1.最新，2.销量（高到低），3.销量（低到高），4.价格(低到高)，5.价格（高到低），6.佣金比例（高到低）
     */
    var sort: String = "new"

}