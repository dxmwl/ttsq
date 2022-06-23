package com.easybuy.mobile.utils

import android.widget.TextView

/**
 * @project : EasyBuy_Android
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/6/23
 */
object FormatUtils {

    /**
     * 格式化销量
     */
    fun formatSales(tv: TextView?, saleNum: String?) {
        if (saleNum == null) {
            tv?.text = "已售10万+件"
        } else {
            try {
                val toLong = saleNum.toLong()
                tv?.text = if (toLong > 5000) {
                    val format = String.format("%.2f", toLong.toDouble() / 10000)
                    "已售${format}万件"
                } else {
                    "已售${toLong}件"
                }
            } catch (e: Exception) {
                tv?.text = "已售10万+件"
            }
        }

    }
}