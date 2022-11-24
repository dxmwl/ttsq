package com.shengqianjun.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * @project : EasyBuy_Android
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/6/23
 */
class HomeBannerApi : IRequestApi {
    override fun getApi(): String {
        return "api/api_lunbo.ashx"
    }

    /**
     * 	分页获取数据,第几页
     */
    var page: Int = 1

    /**
     * 每页数据条数（默认每页20条），可自定义1-50之间
     */
    var page_size: Int = 10

    inner class BannerBean(
        val name: String,
        //图片
        val pic: String,
        //跳转链接
        val get_url: String

    )
}