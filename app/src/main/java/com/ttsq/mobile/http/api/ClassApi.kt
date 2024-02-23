package com.ttsq.mobile.http.api

import android.os.Parcelable
import com.ttsq.mobile.other.AppConfig
import kotlinx.parcelize.Parcelize

/**
 * 分类数据
 */
class ClassApi : HaodankuV2BaseApi() {

    override fun getHost(): String {
        return AppConfig.getHakBaseUrl()
    }

    override fun getApi(): String {
        return "super_classify"
    }

    data class ClassInfo(
        val cid: String,
        val main_name: String,
        val `data`: ArrayList<Data> = ArrayList(),
        var checked: Boolean = false
    )

    @Parcelize
    data class Data(
        val info: ArrayList<Info>,
        val next_name: String
    ) : Parcelable

    @Parcelize
    data class Info(
        val imgurl: String,
        val son_name: String
    ) : Parcelable
}