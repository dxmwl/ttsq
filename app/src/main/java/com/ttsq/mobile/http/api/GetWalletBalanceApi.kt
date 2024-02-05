package com.ttsq.mobile.http.api

import com.hjq.http.config.IRequestApi

/**
 * @ClassName: GetWalletBalanceApi
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/04 0004 14:42
 **/
class GetWalletBalanceApi : IRequestApi {
    override fun getApi(): String {
        return "api/user/getWalletBalance"
    }

    data class WallerBalanceDto(
        val balance: String,
        val totalIncome: String,
        val comingSoonIncome: String,
        val lastMonthIncome: String
    )
}