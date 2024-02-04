package com.ttsq.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * @ClassName: CommitTxAccountApi
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/04 0004 15:14
 **/
class CommitTxAccountApi:IRequestApi {
    override fun getApi(): String {
        return "api/user/commitTxAccount"
    }

    var accountName:String? = null
    var realName:String? = null
}