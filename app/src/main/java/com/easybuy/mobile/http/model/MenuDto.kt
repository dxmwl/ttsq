package com.easybuy.mobile.http.model

import com.easybug.mobile.R

data class MenuDto(
    val resId: Int? = R.mipmap.launcher_ic,
    val title: String? = "",
    val id: String? = "",
    var checked:Boolean = false
)
