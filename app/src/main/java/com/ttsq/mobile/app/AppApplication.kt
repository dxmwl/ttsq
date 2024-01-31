package com.ttsq.mobile.app

import android.app.Application
import android.content.Context
import androidx.core.content.ContextCompat
import com.ttsq.mobile.R
import com.ttsq.mobile.aop.Log
import com.ttsq.mobile.http.glide.GlideApp
import com.ttsq.mobile.http.model.RequestHandler
import com.ttsq.mobile.http.model.RequestServer
import com.ttsq.mobile.manager.ActivityManager
import com.ttsq.mobile.other.*
import com.hjq.bar.TitleBar
import com.hjq.http.EasyConfig
import com.hjq.toast.ToastUtils
import com.hjq.umeng.UmengClient
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.ttsq.mobile.BuildConfig
import com.tencent.mmkv.MMKV
import okhttp3.OkHttpClient
import timber.log.Timber

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 应用入口
 */
class AppApplication : Application() {

    @Log("启动耗时")
    override fun onCreate() {
        super.onCreate()
        _context = this

        initSdk(this)

        UmengClient.preInit(_context, BuildConfig.DEBUG, AppConfig.getChannelTag())
    }

    override fun onLowMemory() {
        super.onLowMemory()
        // 清理所有图片内存缓存
        GlideApp.get(this).onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        // 根据手机内存剩余情况清理图片内存缓存
        GlideApp.get(this).onTrimMemory(level)
    }

    companion object {

        private lateinit var _context: Application

        fun getApp(): Application {
            return _context
        }
    }

    private fun initSdk(application: AppApplication) {

        // 设置标题栏初始化器
        TitleBar.setDefaultStyle(TitleBarStyle())

        // 设置全局的 Header 构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context: Context, layout: RefreshLayout ->
            MaterialHeader(context).setColorSchemeColors(
                ContextCompat.getColor(
                    context,
                    R.color.common_accent_color
                )
            )
        }
        // 设置全局的 Footer 构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context: Context, layout: RefreshLayout ->
            SmartBallPulseFooter(context)
        }
        // 设置全局初始化器
        SmartRefreshLayout.setDefaultRefreshInitializer { context: Context, layout: RefreshLayout ->
            // 刷新头部是否跟随内容偏移
            layout.setEnableHeaderTranslationContent(true)
                // 刷新尾部是否跟随内容偏移
                .setEnableFooterTranslationContent(true)
                // 加载更多是否跟随内容偏移
                .setEnableFooterFollowWhenNoMoreData(true)
                // 内容不满一页时是否可以上拉加载更多
                .setEnableLoadMoreWhenContentNotFull(false)
                // 仿苹果越界效果开关
                .setEnableOverScrollDrag(false)
        }

        // 初始化吐司
        ToastUtils.init(application, ToastStyle())
        // 设置调试模式
        ToastUtils.setDebugMode(AppConfig.isDebug())
        // 设置 Toast 拦截器
        ToastUtils.setInterceptor(ToastLogInterceptor())

        // 本地异常捕捉
        CrashHandler.register(application)

        // Activity 栈管理初始化
        ActivityManager.getInstance().init(application)

        // MMKV 初始化
        MMKV.initialize(application)

        // 网络请求框架初始化
        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .build()

        EasyConfig.with(okHttpClient)
            // 是否打印日志
            .setLogEnabled(AppConfig.isLogEnable())
            // 设置服务器配置
            .setServer(RequestServer())
            // 设置请求处理策略
            .setHandler(RequestHandler(application))
            // 设置请求重试次数
            .setRetryCount(1)
            .addParam("apikey", AppConfig.getHdkAppKey())
            .into()


        // 初始化日志打印
        if (AppConfig.isLogEnable()) {
            Timber.plant(DebugLoggerTree())
        }

        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(true) //（可选）是否显示线程信息。 默认值为true
            .methodCount(2) // （可选）要显示的方法行数。 默认2
            .methodOffset(7) // （可选）设置调用堆栈的函数偏移值，0的话则从打印该Log的函数开始输出堆栈信息，默认是0
            .tag("ttsq") //（可选）每个日志的全局标记。 默认PRETTY_LOGGER（如上图）
            .build()

        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }
}