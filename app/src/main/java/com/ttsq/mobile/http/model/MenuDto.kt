package com.ttsq.mobile.http.model

import com.ttsq.mobile.R

data class MenuDto(
    val id: String = "",
    val resId: Any? = R.mipmap.launcher_ic,
    val title: String? = "",
    val value: String? = "",
    //0:跳转网址  1跳转小程序
    val jumpType:Int = 0,
    var checked:Boolean = false
)
