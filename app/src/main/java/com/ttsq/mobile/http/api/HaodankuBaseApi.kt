package com.ttsq.mobile.http.api

import com.hjq.http.config.IRequestApi
import com.hjq.http.config.IRequestServer

/**
 * Created with Android studio
 * Description:好单库BaseApi
 * @Author: 常利兵
 * DateTime: 2023-02-14 0:02
 */
abstract class HaodankuBaseApi : IRequestApi, IRequestServer {
    override fun getHost(): String {
        return "http://v2.api.haodanku.com/"
    }
}