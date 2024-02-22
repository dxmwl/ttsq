package com.ttsq.mobile.http.api

/**
 * @ClassName: SwitchActivityLinElmApi
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/22 0022 13:28
 **/
class SwitchActivityLinElmApi : HaodankuV3BaseApi() {
    override fun getApi(): String {
        return "elm_activity_ratesurl"
    }

    var activity_id: String? = null
    var sid: String? = null
}