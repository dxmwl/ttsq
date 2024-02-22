package com.ttsq.mobile.http.api

/**
 * @ClassName: SwitchActivityLinkDyApi
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/22 0022 14:43
 **/
class SwitchActivityLinkDyApi : HaodankuV3BaseApi() {
    override fun getApi(): String {
        return "dy_life_share"
    }

    var id: String? = null
    var channel: String? = null

    data class DyLinkDto(
        val dy_deeplink: String,
        val dy_password: String,
        val dy_zlink: String,
        val qr_code: QrCode,
        val share_link: String
    )

    data class QrCode(
        val height: Int,
        val url: String,
        val width: Int
    )
}