package com.ttsq.mobile.http.api

import com.hjq.http.config.IRequestApi
import com.hjq.http.config.IRequestServer
import com.ttsq.mobile.http.model.GoodsDetailDto
import com.ttsq.mobile.http.model.ShopScore
import java.math.BigDecimal

/**
 * @project : EasyBuy_Android
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/6/23
 */
class HomeGoodsListApi : IRequestApi , IRequestServer {
    override fun getApi(): String {
        return "api/maochao_items"
    }

    /**
     * 	分页获取数据,第几页
     */
    var p: Int = 1

    //1.低价包邮；2.猫超热销；3.即将开抢；4.一元购(已下架)
    var type: Int = 2

    //排序：1.价格从低到高；2.价格从高到底；3.日销从高到低；4.日销从低到高；5.2小时销量从高到底；6.两小时销量从低到高；7.最新
    var order: Int = 7
    override fun getHost(): String {
        return "https://api.haodanku.com/"
    }

    /**
     * 每页数据条数（默认每页20条），可自定义1-50之间
     */
//    var page_size: Int = 20

    /**
     * 商品排序方式，new：按照综合排序，
    total_sale_num_asc：按照总销量从小到大排序，total_sale_num_desc：按照总销量从大到小排序，
    sale_num_asc：按照月销量从小到大排序，sale_num_desc：按照月销量从大到小排序，
    commission_rate_asc：按照佣金比例从小到大排序，commission_rate_desc：按照佣金比例从大到小排序，
    price_asc：按照价格从小到大排序，price_desc：按照价格从大到小排序，
    coupon_info_money_asc：按照优惠券金额从小到大排序，coupon_info_money_desc：按照优惠券金额从大到小排序，
    shop_level_asc：按照店铺等级从低到高排序，shop_level_desc：按照店铺等级从高到低排序，
    tkfee_asc：按照返佣金额从低到高排序，tkfee_desc：按照返佣金额从高到低排序，
    code：按照code值从大到小排序，date_time：按照更新时间排序，random：按照随机排序
     */
//    var sort: String = "new"

    /**
     * 	一级商品分类，值为空：全部商品，1：女装，2：母婴，3：美妆，4：居家日用，5：鞋品，6：美食，7：文娱车品，8：数码家电，9：男装，10：内衣，11：箱包，12：配饰，13：户外运动，14：家装家纺
     */
//    var cid: String = ""

    /**
     * 	极品爆单商品，值为空：全部商品，1：极品爆单商品（拍2件、拍3件、拍4件、拍5件、拍6件等）
     */
//    var baodan: String = ""

    inner class MaochaoGoods(
        val data :ArrayList<GoodsDetailDto>
    )
}