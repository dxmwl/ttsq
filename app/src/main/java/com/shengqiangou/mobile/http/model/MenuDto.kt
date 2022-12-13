package com.shengqiangou.mobile.http.model

import com.shengqiangou.mobile.R

data class MenuDto(
    val id: String = "",
    val resId: Any? = R.mipmap.launcher_ic,
    val title: String? = "",
    val value: String? = "",
    var checked:Boolean = false
)
