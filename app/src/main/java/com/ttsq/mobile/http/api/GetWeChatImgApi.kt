package com.ttsq.mobile.http.api

/**
 * 二维码发单图
 */
class GetWeChatImgApi : HaodankuV2BaseApi() {
    override fun getApi(): String {
        return "api/open_qrpic.ashx"
    }

    //淘口令
    var content = ""

    val type = 0

    data class WechatImgDto(
        val copy_tkl_url: String,
        val copy_tkl_url2: String,
        val item_id: String,
        val model: String,
        val pic_url: String,
        val status: Int,
        val wenan: String
    )
}