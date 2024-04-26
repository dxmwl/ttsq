package com.ttsq.mobile.other

import android.content.pm.PackageManager
import com.ttsq.mobile.BuildConfig
import com.ttsq.mobile.app.AppApplication
import com.ttsq.mobile.app.Constants
import com.ttsq.mobile.app.Constants.URL_DOWNLOAD

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
     * 获取 淘宝联盟Pid
     */
    fun getTblmPid(): String {
        return BuildConfig.TBLM_PID
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
     * 获取渠道标记
     */
    fun getChannelTag(): String {
        var channelName = "guanfang"
        try {
            val packageManager = AppApplication.getApp().packageManager;
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                val applicationInfo = packageManager.getApplicationInfo(
                    AppApplication.getApp().packageName,
                    PackageManager.GET_META_DATA
                );
                if (applicationInfo.metaData != null) {
                    channelName = applicationInfo.metaData.getString("UMENG_CHANNEL") ?: "guanfang"
                }
            }
        } catch (e: Exception) {
            e.printStackTrace();
        }
        return channelName;
    }

    /**
     * 获取用户协议链接
     */
    fun getUserAgreementUrl(): String {
//        return if (getChannelTag()=="vivo"){
//            Constants.URL_USER_AGREEMENT_VIVO
//        }else{
        return Constants.URL_USER_AGREEMENT
//        }
    }

    /**
     * 获取隐私政策链接
     */
    fun getPrivacyPolicyUrl(): String {
//        return if (getChannelTag() == "vivo") {
//            Constants.URL_PRIVACY_POLICY_VIVO
//        } else {
        return Constants.URL_PRIVACY_POLICY
//        }
    }

    /**
     * 下载地址
     */
    fun getDownloadUrl(): String {
        return URL_DOWNLOAD
    }

    /**
     * 返利比例
     */
    var rebateRate:String = "0.70"

    /**
     * 获取存储桶
     */
    fun getBucketName(): String {
        return BuildConfig.BUCKET_NAME
    }
}