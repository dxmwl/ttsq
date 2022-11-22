package com.easybuy.mobile.http.model

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2019/12/07
 *    desc   : 统一接口数据结构
 */
open class HttpData<T> {

    /** 返回码 */
    private val status: Int = 0

    /**
     * 返回码 好单库使用
     */
    private val code: Int = 0

    /** 提示语 */
    private val msg: String? = null

    /** 数据 */
    private val content: T? = null

    /**
     * 数据(好单库分类接口使用)
     */
    private val general_classify: T? = null

    /**
     * 数据(折淘客分类接口使用)
     */
    private val type: T? = null

    /**
     * 数据(折淘客淘口令创建接口使用)
     */
    private val model: T? = null

    /**
     * 数据(折淘客联想词接口使用)
     */
    private val result: T? = null

    /**
     * 数据(折淘客二维码发图接口使用)
     */
    private val pic_url: T? = null

    /**
     * 数据(折淘客猜你喜欢接口使用)
     */
    private val tbk_dg_optimus_material_response: T? = null

    fun getCode(): Int {
        return status
    }

    fun getMessage(): String? {
        return msg
    }

    fun getData(): T? {
        return if (content != null) {
            content
        } else if (pic_url != null) {
            pic_url
        } else if (general_classify != null) {
            general_classify
        } else if (result != null) {
            result
        } else if (model != null) {
            model
        } else if (tbk_dg_optimus_material_response != null) {
            tbk_dg_optimus_material_response
        } else {
            type
        }
    }

    /**
     * 是否请求成功
     */
    fun isRequestSucceed(): Boolean {
        return status == 200 || code == 1 || (status == 0 && code == 0)
    }

    /**
     * 是否 Token 失效
     */
    fun isTokenFailure(): Boolean {
        return status == 1001
    }
}