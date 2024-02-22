package com.ttsq.mobile.http.api

/**
 * @ClassName: GetElmListApi
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/22 0022 13:25
 **/
class GetElmListApi: HaodankuV3BaseApi() {
    override fun getApi(): String {
        return "elm_activity_list"
    }
}