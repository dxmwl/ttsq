package com.shengqianjun.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * @project : EasyBuy_Android
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/6/23
 */
class SearchGoodsApi : IRequestApi {
    override fun getApi(): String {
        return "api/api_quanwang.ashx"
    }

    //搜索关键字
    var q: String = ""

    var page: Int = 1

    var page_size: Int = 20

    //	是否有券，1为有券，其它值为全部商品
    var youquan: Int = 1

    /**
     * 商品排序方式，new：按照综合排序，
    total_sale_num_asc：按照总销量从小到大排序，total_sale_num_desc：按照总销量从大到小排序，
    sale_num_asc：按照月销量从小到大排序，sale_num_desc：按照月销量从大到小排序，
    commission_rate_asc：按照佣金比例从小到大排序，commission_rate_desc：按照佣金比例从大到小排序，
    price_asc：按照价格从小到大排序，price_desc：按照价格从大到小排序。
     */
    var sort: String = "new"

}