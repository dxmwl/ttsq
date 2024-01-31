package com.ttsq.mobile.ui.activity

import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.airbnb.lottie.LottieAnimationView
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SpanUtils
import com.bytedance.sdk.openadsdk.AdSlot
import com.bytedance.sdk.openadsdk.CSJAdError
import com.bytedance.sdk.openadsdk.CSJSplashAd
import com.bytedance.sdk.openadsdk.CSJSplashCloseType
import com.bytedance.sdk.openadsdk.TTAdConfig
import com.bytedance.sdk.openadsdk.TTAdNative
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.bytedance.sdk.openadsdk.mediation.init.MediationConfig
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonToken
import com.ttsq.mobile.R
import com.ttsq.mobile.app.AppActivity
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.hjq.gson.factory.GsonFactory
import com.hjq.http.EasyConfig
import com.hjq.toast.ToastUtils
import com.hjq.umeng.UmengClient
import com.hjq.widget.view.SlantedTextView
import com.orhanobut.logger.Logger
import com.tencent.bugly.crashreport.CrashReport
import com.ttsq.mobile.app.AppApplication
import com.ttsq.mobile.manager.ActivityManager
import com.ttsq.mobile.manager.UserManager
import com.ttsq.mobile.other.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 闪屏界面
 */
class SplashActivity : AppActivity() {

    private val lottieView: LottieAnimationView? by lazy { findViewById(R.id.lav_splash_lottie) }
    private val debugView: SlantedTextView? by lazy { findViewById(R.id.iv_splash_debug) }
    private val mSplashContainer: FrameLayout? by lazy { findViewById(R.id.ad_view) }
    //@[classname]
    private var csjSplashAdListener: TTAdNative.CSJSplashAdListener? = null

    //@[classname]
    private var splashAdListener: CSJSplashAd.SplashAdListener? = null

    //@[classname]
    private var csjSplashAd: CSJSplashAd? = null

    override fun getLayoutId(): Int {
        return R.layout.splash_activity
    }

    override fun initView() {

        UserManager.init()

        // 设置动画监听
//        lottieView?.addAnimatorListener(object : AnimatorListenerAdapter() {
//            override fun onAnimationEnd(animation: Animator?) {
//                lottieView?.removeAnimatorListener(this)
//                HomeActivity.start(this@SplashActivity)
//                finish()
//            }
//        })
        if (!SPUtils.getInstance("APP_CONFIG").getBoolean("AGREE_AGREEMENT", false)) {
            showYszcDialog()
        } else {
            checkLogin(AppApplication.getApp())
        }
    }

    override fun initData() {
        debugView?.let {
            it.setText(AppConfig.getBuildType().uppercase(Locale.getDefault()))
            if (AppConfig.isDebug()) {
                it.visibility = View.VISIBLE
            } else {
                it.visibility = View.INVISIBLE
            }
        }
    }

    override fun createStatusBarConfig(): ImmersionBar {
        return super.createStatusBarConfig()
            // 隐藏状态栏和导航栏
            .hideBar(BarHide.FLAG_HIDE_BAR)
    }

    override fun onBackPressed() {
        // 禁用返回键
        //super.onBackPressed();
    }

    override fun initActivity() {
        // 问题及方案：https://www.cnblogs.com/net168/p/5722752.html
        // 如果当前 Activity 不是任务栈中的第一个 Activity
        if (!isTaskRoot) {
            val intent: Intent? = intent
            // 如果当前 Activity 是通过桌面图标启动进入的
            if (((intent != null) && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
                        && (Intent.ACTION_MAIN == intent.action))
            ) {
                // 对当前 Activity 执行销毁操作，避免重复实例化入口
                finish()
                return
            }
        }
        super.initActivity()
    }

    /**
     * 展示隐私政策弹框
     */
    private fun showYszcDialog() {
        val dialog = Dialog(this, R.style.BottomDialog)
        val contentView = LayoutInflater.from(this).inflate(R.layout.dialog_yszc_layout, null)
        dialog.setContentView(contentView)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        val layoutParams = contentView.layoutParams
        layoutParams.width = resources.displayMetrics.widthPixels
        contentView.layoutParams = layoutParams
        dialog.window?.setWindowAnimations(R.style.BottomDialog_Animation)
        val tvTitle = contentView.findViewById<TextView>(R.id.title)
        val tvMessage = contentView.findViewById<TextView>(R.id.message)
        val negativeButton = contentView.findViewById<TextView>(R.id.negativeButton)
        val positiveButton = contentView.findViewById<TextView>(R.id.positiveButton)
        negativeButton.text = "拒绝并退出"
        positiveButton.text = "同意"
        tvTitle.text = "用户协议和隐私政策"
        SpanUtils.with(tvMessage)
            .append("亲爱的用户，本软件的正常使用需要依法征用您的登录身份个人信息，本平台承诺将严格保护您个人信息，确保信息安全，具体详见我方按照相关法律法规要求制定的")
            .append("《服务协议》")
            .setClickSpan(Color.parseColor("#FF0690FE"), false) {
                BrowserActivity.start(this, AppConfig.getUserAgreementUrl())
            }
            .append("及")
            .append("《隐私政策》")
            .setClickSpan(Color.parseColor("#FF0690FE"), false) {
                BrowserActivity.start(this, AppConfig.getPrivacyPolicyUrl())
            }.create()
        dialog.show()

        positiveButton.setOnClickListener {
            checkLogin(AppApplication.getApp())
            SPUtils.getInstance("APP_CONFIG").put("AGREE_AGREEMENT", true)
            dialog.dismiss()
        }
        negativeButton.setOnClickListener {
            dialog.dismiss()
            // 移动到上一个任务栈，避免侧滑引起的不良反应
            moveTaskToBack(false)
            postDelayed({
                // 进行内存优化，销毁掉所有的界面
                ActivityManager.getInstance().finishAllActivities()
            }, 300)
        }
    }

    /**
     * 初始化一些第三方框架
     */
    private fun checkLogin(application: Application) {
        initCsjSdk(application)
        // 友盟统计、登录、分享 SDK
        UmengClient.init(application, AppConfig.isLogEnable(), AppConfig.getChannelTag())

        // Bugly 异常捕捉
        CrashReport.initCrashReport(application, AppConfig.getBuglyId(), AppConfig.isDebug())

        // 设置 Json 解析容错监听
        GsonFactory.setJsonCallback { typeToken: TypeToken<*>, fieldName: String?, jsonToken: JsonToken ->
            // 上报到 Bugly 错误列表
            CrashReport.postCatchedException(IllegalArgumentException("类型解析异常：$typeToken#$fieldName，后台返回的类型为：$jsonToken"))
        }

        // 注册网络状态变化监听
        val connectivityManager: ConnectivityManager? =
            ContextCompat.getSystemService(application, ConnectivityManager::class.java)
        if (connectivityManager != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(object :
                ConnectivityManager.NetworkCallback() {
                override fun onLost(network: Network) {
                    val topActivity: Activity? = ActivityManager.getInstance().getTopActivity()
                    if (topActivity !is LifecycleOwner) {
                        return
                    }
                    val lifecycleOwner: LifecycleOwner = topActivity
                    if (lifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED) {
                        return
                    }
                    ToastUtils.show(R.string.common_network_error)
                }
            })
        }
    }

    /**
     * 初始化穿山甲
     */
    private fun initCsjSdk(application: Application?) {
        val configJsonStr =
            "{\"cypher\":2,\"message\":\"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\"}"
        var configJsonObj: JSONObject? = null
        try {
            configJsonObj = JSONObject(configJsonStr)
        } catch (e: JSONException) {
            Logger.e("初始化穿山甲默认配置json失败")
        }

        val sdkVersion = TTAdSdk.getAdManager().getSDKVersion()
        Logger.d("SDK_VERSION:${sdkVersion}")
        TTAdSdk.init(
            application, TTAdConfig.Builder()
                .appId("5465943")
                .useMediation(true)//开启聚合功能，默认false
                .debug(AppConfig.isDebug())
                .setMediationConfig(
                    MediationConfig.Builder()
                        .setCustomLocalConfig(configJsonObj)
                        .build()
                )
                .build()
        )
        TTAdSdk.start(object : TTAdSdk.Callback {
            override fun success() {
                Logger.d("CSJAD_success:" + TTAdSdk.isSdkReady())

                loadAd()
            }

            override fun fail(p0: Int, p1: String?) {
                Logger.d("CSJAD_fail:${p0}, ${p1}")
                jumpActivity()
            }
        })
    }

    fun loadAd() {
        val appScreenWidth = ScreenUtils.getScreenWidth()
        val appScreenHeight = ScreenUtils.getScreenHeight()
        Logger.d("屏幕宽高：${appScreenWidth}, ${appScreenHeight}")
        /** 1、创建AdSlot对象 */
        //@[classname]
        val adslot = AdSlot.Builder()
            .setCodeId("102635665")
            .setImageAcceptedSize(
                appScreenWidth,
                appScreenHeight
            )
            .build()

        /** 2、创建TTAdNative对象 */
        //@[classname]//@[methodname]
        val adNativeLoader = TTAdSdk.getAdManager().createAdNative(this@SplashActivity)

        /** 3、创建加载、展示监听器 */
        initListeners()

        /** 4、加载广告  */
        adNativeLoader.loadSplashAd(adslot, csjSplashAdListener, 3500)
    }

    //@[classname]
    fun showAd(csjSplashAd: CSJSplashAd?) {
        /** 5、渲染成功后，展示广告 */
        this.csjSplashAd = csjSplashAd
        csjSplashAd?.setSplashAdListener(splashAdListener)
        csjSplashAd?.let {
            it.splashView?.let { splashView ->
                mSplashContainer?.addView(splashView)
            }
        }
    }

    private fun initListeners() {
        // 广告加载监听器
        //@[classname]
        csjSplashAdListener = object : TTAdNative.CSJSplashAdListener {
            //@[classname]
            override fun onSplashLoadSuccess(ad: CSJSplashAd?) {
                Logger.d("onSplashAdLoad")
            }

            //@[classname]
            override fun onSplashLoadFail(error: CSJAdError?) {
                Logger.d("onError code = ${error?.code} msg = ${error?.msg}")
                jumpActivity()
            }

            //@[classname]
            override fun onSplashRenderSuccess(ad: CSJSplashAd?) {
                Logger.d("onSplashRenderSuccess")
                showAd(ad)
            }

            //@[classname]
            override fun onSplashRenderFail(ad: CSJSplashAd?, error: CSJAdError?) {
                Logger.d("onError code = ${error?.code} msg = ${error?.msg}")
                jumpActivity()
            }
        }
        //@[classname]
        splashAdListener = object : CSJSplashAd.SplashAdListener {
            //@[classname]
            override fun onSplashAdShow(p0: CSJSplashAd?) {
                Logger.d("onSplashAdShow")
            }

            //@[classname]
            override fun onSplashAdClick(p0: CSJSplashAd?) {
                Logger.d("onSplashAdClick")
            }

            //@[classname]
            override fun onSplashAdClose(p0: CSJSplashAd?, closeType: Int) {
                //@[classname]
                if (closeType == CSJSplashCloseType.CLICK_SKIP) {
                    Logger.d("开屏广告点击跳过")
                    //@[classname]
                } else if (closeType == CSJSplashCloseType.COUNT_DOWN_OVER) {
                    Logger.d("开屏广告点击倒计时结束")
                    //@[classname]
                } else if (closeType == CSJSplashCloseType.CLICK_JUMP) {
                    Logger.d("点击跳转")
                }
                jumpActivity()
            }
        }
    }

    private fun jumpActivity() {
        if (UserManager.tokenResult == null) {
            startActivity(LoginActivity::class.java)
        } else {
            // 更新 Token
            EasyConfig.getInstance()
                .addHeader("Authorization", "Bearer ${UserManager.tokenResult?.getToken()}")
            HomeActivity.start(this@SplashActivity)
        }
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        /** 6、在onDestroy中销毁广告  */
        csjSplashAd?.mediationManager?.destroy()
    }
}