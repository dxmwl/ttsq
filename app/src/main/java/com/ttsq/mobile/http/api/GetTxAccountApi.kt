package com.ttsq.mobile.http.api

import com.hjq.http.config.IRequestApi

class GetTxAccountApi :IRequestApi{
    override fun getApi(): String {
        return "api/user/getTxAccount"
    }

    data class TxAccountDto(
        val zhifubaoAccount:String,
        val realName:String,
    )
}
