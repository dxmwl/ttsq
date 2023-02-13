package com.ttsq.mobile.http.api

/**
 * @project : EasyBuy_Android
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/6/23
 */
class HomeBannerApi : HaodankuBaseApi() {
    override fun getApi(): String {
        return "get_index_activity_data"
    }

    /**
     * 	分页获取数据,第几页
     */
//    var page: Int = 1

    /**
     * 每页数据条数（默认每页20条），可自定义1-50之间
     */
    var page_size: Int = 10

    data class BannerBean(
        val activity_id: String,
        val activity_title: String,
        val image: String,
    )
}