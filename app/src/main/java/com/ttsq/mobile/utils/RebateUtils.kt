package com.ttsq.mobile.utils

import com.ttsq.mobile.other.AppConfig
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * @ClassName: RebateUtils
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/04 0004 11:44
 **/
object RebateUtils {

    /**
     * 计算返利
     */
    fun calculateRebate(tkmoney: String?): String {
        if (tkmoney.isNullOrBlank()) {
            return "0"
        }
        return "${
            BigDecimal(tkmoney).multiply(BigDecimal(AppConfig.rebateRate))
                .setScale(2, RoundingMode.HALF_UP)
        }"
    }
}