package com.ttsq.mobile.http.api


class ClassPinpaiListApi : HaodankuV2BaseApi()  {
    override fun getApi(): String {
        return "brand"
    }

    var back = 20
    var min_id = 1
    var brandcat = "1"

    data class PinpaiListDto(
        val brand_id: String,
        val brand_image: String,
        val brand_logo: String,
        val tb_brand_name: String,
        val brand_plain: String,
        val id: String,
        val item: ArrayList<RecommendPinpaiApi.Item>,
    )

}