package com.ttsq.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * @ClassName: CommitTxApi
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/04 0004 15:45
 **/
class CommitTxApi : IRequestApi {
    override fun getApi(): String {
        return "api/user/commitTx"
    }

    var txMoney: String? = null
}