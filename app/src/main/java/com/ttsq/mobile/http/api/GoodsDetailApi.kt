package com.ttsq.mobile.http.api

/**
 * @project : EasyBuy_Android
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/6/23
 */
class GoodsDetailApi : HaodankuBaseApi() {

    //	单个商品ID
    //该参数支持全网所有淘宝客商品。
    var itemid: String = ""

    //折淘客编号，输入非折淘客商品编号，此参数无作用。code值可通过其它领券API接口结果获得。code值必须与tao_id值对应。
    var code: String = ""

    override fun getApi(): String {
        return "item_detail"
    }
}