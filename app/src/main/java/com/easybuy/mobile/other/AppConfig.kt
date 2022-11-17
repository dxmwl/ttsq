package com.easybuy.mobile.other

import com.easybug.mobile.BuildConfig
/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2019/09/02
 *    desc   : App 配置管理类
 */
object AppConfig {

    /**
     * 当前是否为调试模式
     */
    fun isDebug(): Boolean {
        return BuildConfig.DEBUG
    }

    /**
     * 获取当前构建的模式
     */
    fun getBuildType(): String {
        return BuildConfig.BUILD_TYPE
    }

    /**
     * 当前是否要开启日志打印功能
     */
    fun isLogEnable(): Boolean {
        return BuildConfig.LOG_ENABLE
    }

    /**
     * 获取当前应用的包名
     */
    fun getPackageName(): String {
        return BuildConfig.APPLICATION_ID
    }

    /**
     * 获取当前应用的版本名
     */
    fun getVersionName(): String {
        return BuildConfig.VERSION_NAME
    }

    /**
     * 获取当前应用的版本码
     */
    fun getVersionCode(): Int {
        return BuildConfig.VERSION_CODE
    }

    /**
     * 获取 Bugly Id
     */
    fun getBuglyId(): String {
        return BuildConfig.BUGLY_ID
    }

    /**
     * 获取服务器主机地址
     */
    fun getHostUrl(): String {
        return BuildConfig.HOST_URL
    }

    /**
     * 获取 折淘客AppKey
     */
    fun getZtkAppKey(): String {
        return BuildConfig.ZTK_APP_KEY
    }

    /**
     * 获取 淘宝联盟Pid
     */
    fun getTblmPid(): String {
        return BuildConfig.TBLM_PID
    }

    /**
     * 获取 折淘客SqId
     */
    fun getZtkSqId(): String {
        return BuildConfig.ZTK_SQ_ID
    }

    /**
     * 获取 好单库请求地址
     */
    fun getHakBaseUrl(): String {
        return BuildConfig.hak_baseUrl
    }

    /**
     * 获取 好单库请求key
     */
    fun getHdkAppKey(): String {
        return BuildConfig.HDK_APP_KEY
    }

    /**
     * 9块9包邮链接
     */
    fun getJiukuaijiuUrl(): String {
        return "http://cms.xeemm.com:10000/jiudianjiu.aspx?id=27660&sid=${getZtkSqId()}&relationId="
    }
}