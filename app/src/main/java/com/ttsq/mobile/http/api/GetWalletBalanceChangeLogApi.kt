package com.ttsq.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * @ClassName: GetWalletBalanceChangeLogApi
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/04 0004 17:17
 **/
class GetWalletBalanceChangeLogApi : IRequestApi {
    override fun getApi(): String {
        return "api/user/getWalletChangeLog"
    }

    data class WalletBalanceDto(
        val type: Int,
        val balance: String,
        val incomeMoney: String,
        val createTime: String,
    )
}