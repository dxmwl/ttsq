package com.ttsq.mobile.http.api

/**
 * @ClassName: SwitchActivityLinkApi
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/21 0021 16:28
 **/
class SwitchActivityLinMeituanApi : HaodankuV3BaseApi() {
    override fun getApi(): String {
        return "meituan_ratesurl"
    }

    var activity_id: String? = null

    /**
     * 链接类型：
     * 1、h5链接；
     * 2、deeplink(唤起)链接；
     * 3、中间页唤起链接；
     * 4、微信小程序唤起路径（默认1）
     */
    val link_type = 1

    data class SwitchActivityLinkDto(
        val url: String,
        val h5_short_link: String
    )
}