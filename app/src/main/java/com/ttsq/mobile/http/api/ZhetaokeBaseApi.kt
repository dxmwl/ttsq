package com.ttsq.mobile.http.api

import com.hjq.http.config.IRequestApi
import com.hjq.http.config.IRequestHost

/**
 * Created with Android studio
 * Description:折淘客Base接口
 * @Author: 常利兵
 * DateTime: 2023-02-13 23:56
 */
abstract class ZhetaokeBaseApi: IRequestApi, IRequestHost {

    val appkey = "2506f1398595428eab9055197bf671e7"
    val signurl = 5

    val sid = "154994"
    val pid = "mm_111203980_10278226_34170938"


    override fun getHost(): String {
        return "https://api.zhetaoke.com:10001/"
    }
}