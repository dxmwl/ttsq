package com.easybuy.mobile.http.model

import com.easybug.mobile.R

data class MenuDto(
    val id: String = "",
    val resId: Any? = R.mipmap.launcher_ic,
    val title: String? = "",
    val value: String? = "",
    var checked:Boolean = false
)
