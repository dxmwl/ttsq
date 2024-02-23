package com.ttsq.mobile.http.api

/**
 * @ClassName: GetDyGoodsClassifyApi
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/22 0022 13:44
 **/
class GetDyGoodsClassifyApi : HaodankuV3BaseApi() {
    override fun getApi(): String {
        return "dy_life_category"
    }

    data class GoodsClasifyDto(
        val list: List<GoodsClassifyDyDto>
    )

    data class GoodsClassifyDyDto(
        val children: List<Children>,
        val title: String,
        val `val`: Int
    )

    data class Children(
        val title: String,
        val `val`: Int
    )
}