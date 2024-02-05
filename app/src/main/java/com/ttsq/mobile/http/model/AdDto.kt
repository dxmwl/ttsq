package com.ttsq.mobile.http.model

/**
 * @ClassName: AdDto
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/1/31 0031 20:34
 **/
data class AdDto(val type: DataType = DataType.DATE, val data: Any)

enum class DataType {
    //广告
    AD,
    //数据
    DATE
}
