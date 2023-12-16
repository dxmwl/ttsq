package com.ttsq.mobile.ui.activity

import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.airbnb.lottie.LottieAnimationView
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.SpanUtils
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonToken
import com.ttsq.mobile.R
import com.ttsq.mobile.app.AppActivity
import com.ttsq.mobile.http.api.UserInfoApi
import com.ttsq.mobile.http.model.HttpData
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.TitleBar
import com.hjq.gson.factory.GsonFactory
import com.hjq.http.EasyConfig
import com.hjq.http.EasyHttp
import com.hjq.http.listener.HttpCallback
import com.hjq.toast.ToastUtils
import com.hjq.umeng.UmengClient
import com.hjq.widget.view.SlantedTextView
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.mmkv.MMKV
import com.ttsq.mobile.BuildConfig
import com.ttsq.mobile.app.AppApplication
import com.ttsq.mobile.manager.ActivityManager
import com.ttsq.mobile.manager.UserManager
import com.ttsq.mobile.other.*
import okhttp3.OkHttpClient
import timber.log.Timber
import java.lang.Boolean.getBoolean
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
        postDelayed({
            if (!SPUtils.getInstance("APP_CONFIG").getBoolean("AGREE_AGREEMENT", false)) {
                showYszcDialog()
            } else {
                initSdk(AppApplication.getApp())
                HomeActivity.start(this@SplashActivity)
            }
        }, 5000)
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
            initSdk(AppApplication.getApp())
            HomeActivity.start(this@SplashActivity)
            SPUtils.getInstance("APP_CONFIG").put("AGREE_AGREEMENT", true)
            finish()
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

    override fun onDestroy() {
        // 因为修复了一个启动页被重复启动的问题，所以有可能 Activity 还没有初始化完成就已经销毁了
        // 所以如果需要在此处释放对象资源需要先对这个对象进行判空，否则可能会导致空指针异常
        super.onDestroy()
    }

    /**
     * 初始化一些第三方框架
     */
    fun initSdk(application: Application) {

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
}