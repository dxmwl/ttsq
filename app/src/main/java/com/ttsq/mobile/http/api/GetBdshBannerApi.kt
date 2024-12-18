package com.ttsq.mobile.http.api

/**
 * @ClassName: GetBdshBannerApi
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/21 0021 15:33
 **/
class GetBdshBannerApi : HaodankuV3BaseApi() {
    override fun getApi(): String {
        return "meituan_activity_list"
    }

    data class BdshBannerDto(
        val red_activity: ArrayList<ActivityDto>,
        val time_activity: ArrayList<ActivityDto>
    )

    data class ActivityDto(
        val activity_desc_private: String,
        val activity_id: String,
        val activity_image: String,
        val activity_name: String,
        val end_time: String,
        val start_time: String
    )
}