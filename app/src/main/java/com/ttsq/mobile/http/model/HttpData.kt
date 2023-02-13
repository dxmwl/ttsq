package com.ttsq.mobile.http.model

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2019/12/07
 *    desc   : 统一接口数据结构
 */
open class HttpData<T> {

    /**
     * 返回码 好单库使用
     */
    private val code: Int = 0

    /**
     * 省钱宝接口使用
     */
    private val status: Int = 0

    /** 提示语 */
    private val msg: String? = null

    /** 数据 */
    private val data: T? = null

    /**
     * 数据(折淘客联想词接口用)
     */
    private val result: T? = null

    /**
     * 数据(好单库分类接口使用)
     */
    private val general_classify: T? = null

    /**
     * 省钱宝接口使用
     */
    private val content: T? = null


    fun getCode(): Int {
        return code
    }

    fun getMessage(): String? {
        return msg
    }

    fun getData(): T? {
        return if (data != null) {
            data
        } else if (general_classify != null) {
            general_classify
        } else if (content != null) {
            content
        } else if (result != null) {
            result
        } else {
            data
        }
    }

    /**
     * 是否请求成功
     */
    fun isRequestSucceed(): Boolean {
        return code == 1 || code == 200 || status == 200 || code == 0
    }

    /**
     * 是否 Token 失效
     */
    fun isTokenFailure(): Boolean {
        return code == 1001
    }
}