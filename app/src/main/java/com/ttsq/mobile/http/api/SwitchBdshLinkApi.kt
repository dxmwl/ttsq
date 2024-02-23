package com.ttsq.mobile.http.api

/**
 * @ClassName: SwitchBdshLinkApi
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/22 0022 12:27
 **/
class SwitchBdshLinkApi: HaodankuV3BaseApi() {
    override fun getApi(): String {
        return "hv_ratesurl"
    }

    var platform:String? = null
    var channel:String? = null

    data class SwitchBdshLinkDto(
        val name:String,
        val link:String,
    )
}