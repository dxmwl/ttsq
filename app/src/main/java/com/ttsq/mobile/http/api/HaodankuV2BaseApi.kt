package com.ttsq.mobile.http.api

import com.hjq.http.config.IRequestApi
import com.hjq.http.config.IRequestServer
import com.ttsq.mobile.BuildConfig
import com.ttsq.mobile.other.AppConfig
import java.text.SimpleDateFormat

/**
 * Created with Android studio
 * Description:好单库BaseApi
 * @Author: 常利兵
 * DateTime: 2023-02-14 0:02
 */
abstract class HaodankuV2BaseApi : IRequestApi, IRequestServer {

    var pid = AppConfig.getTblmPid()
    var tb_name: String = "魅影天利时代"

    override fun getHost(): String {
        return "http://v2.api.haodanku.com/"
    }
}