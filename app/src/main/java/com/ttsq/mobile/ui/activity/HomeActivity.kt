package com.ttsq.mobile.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AppOpsManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ClipboardUtils
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ScreenUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
//import com.didichuxing.doraemonkit.DoKit
import com.gyf.immersionbar.ImmersionBar
import com.hjq.base.BaseDialog
import com.hjq.base.FragmentPagerAdapter
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.kwad.sdk.core.b.a.it
import com.orhanobut.logger.Logger
import com.ttsq.mobile.R
import com.ttsq.mobile.app.AppActivity
import com.ttsq.mobile.app.AppApplication
import com.ttsq.mobile.app.AppFragment
import com.ttsq.mobile.app.AppHelper
import com.ttsq.mobile.eventbus.RefreshClass
import com.ttsq.mobile.http.api.ClassApi
import com.ttsq.mobile.http.api.GetCommonConfigApi
import com.ttsq.mobile.http.api.GetNewAppInfoApi
import com.ttsq.mobile.http.api.GetYouhuiApi
import com.ttsq.mobile.http.api.UserInfoApi
import com.ttsq.mobile.http.model.HttpData
import com.ttsq.mobile.manager.ActivityManager
import com.ttsq.mobile.manager.UserManager
import com.ttsq.mobile.other.AppConfig
import com.ttsq.mobile.other.DoubleClickHelper
import com.ttsq.mobile.ui.adapter.NavigationAdapter
import com.ttsq.mobile.ui.dialog.MessageDialog
import com.ttsq.mobile.ui.dialog.UpdateDialog
import com.ttsq.mobile.ui.fragment.*
import com.ttsq.mobile.utils.UpdateChecker
import com.ttsq.mobile.utils.livebus.LiveDataBus
import com.umeng.message.PushAgent
import com.umeng.message.inapp.InAppMessageManager
import org.greenrobot.eventbus.EventBus
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException


/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 首页界面
 */
class HomeActivity : AppActivity(), NavigationAdapter.OnNavigationListener {

    companion object {

        private const val INTENT_KEY_IN_FRAGMENT_INDEX: String = "fragmentIndex"
        private const val INTENT_KEY_IN_FRAGMENT_CLASS: String = "fragmentClass"

        @JvmOverloads
        fun start(
            context: Context,
            fragmentClass: Class<out AppFragment<*>?>? = HomeFragment::class.java
        ) {
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra(INTENT_KEY_IN_FRAGMENT_CLASS, fragmentClass)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private val viewPager: ViewPager? by lazy { findViewById(R.id.vp_home_pager) }
    private val navigationView: RecyclerView? by lazy { findViewById(R.id.rv_home_navigation) }
    private var navigationAdapter: NavigationAdapter? = null
    private var pagerAdapter: FragmentPagerAdapter<AppFragment<*>>? = null

    override fun getLayoutId(): Int {
        return R.layout.home_activity
    }

    override fun initView() {
        PushAgent.getInstance(this).onAppStart()

        navigationAdapter = NavigationAdapter(this).apply {
            addItem(
                NavigationAdapter.MenuItem(
                    getString(R.string.home_nav_index),
                    ContextCompat.getDrawable(this@HomeActivity, R.drawable.home_home_selector)
                )
            )
            addItem(
                NavigationAdapter.MenuItem(
                    getString(R.string.home_nav_found),
                    ContextCompat.getDrawable(this@HomeActivity, R.drawable.home_found_selector)
                )
            )
            addItem(
                NavigationAdapter.MenuItem(
                    getString(R.string.home_nav_bdsh),
                    ContextCompat.getDrawable(this@HomeActivity, R.drawable.home_bdsh_selector)
                )
            )
            addItem(
                NavigationAdapter.MenuItem(
                    getString(R.string.home_nav_message),
                    ContextCompat.getDrawable(this@HomeActivity, R.drawable.home_message_selector)
                )
            )
            addItem(
                NavigationAdapter.MenuItem(
                    getString(R.string.home_nav_me),
                    ContextCompat.getDrawable(this@HomeActivity, R.drawable.home_me_selector)
                )
            )
            setOnNavigationListener(this@HomeActivity)
            navigationView?.adapter = this
        }

//        DoKit.Builder(AppApplication.getApp())
//            .productId("2f3d442feec5bff93d8c643c6612d833")
//            .build()

        //展示插屏消息
        InAppMessageManager.getInstance(this).showCardMessage(
            this, "main"
        ) { }
    }

    override fun initData() {
        pagerAdapter = FragmentPagerAdapter<AppFragment<*>>(this).apply {
            addFragment(HomeFragment.newInstance())
            addFragment(BangdanFragment.newInstance())
            addFragment(BdshFragment.newInstance())
            addFragment(YqbkFragment.newInstance())
            addFragment(MeFragment.newInstance())
            viewPager?.adapter = this
        }
        onNewIntent(intent)
        getNewAppInfo()
        getCommonConfig()
        getClassData()

        LiveDataBus.subscribe("refreshUserInfo", this) { data ->
            getUserInfo()
        }
        val notificationEnabled = isNotificationEnabled()
        if (notificationEnabled.not()) {
            MessageDialog.Builder(this)
                .setTitle("通知开关")
                .setMessage("开启通知开关，可以收到更多优惠信息哦！")
                .setConfirm("开启")
                .setListener(object : MessageDialog.OnListener {
                    override fun onConfirm(dialog: BaseDialog?) {
                        openNotificationSettings()
                    }
                })
                .show()
        }
    }

    // 打开通知权限设置页面
    private fun openNotificationSettings() {
        val intent = Intent()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Android 8.0 及以上版本
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName())
        } else
        // Android 5.0 及以上版本
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS")
        intent.putExtra("app_package", getPackageName())
        intent.putExtra("app_uid", AppApplication.getApp().applicationInfo.uid)
        startActivity(intent)
    }

    // 检查通知权限状态
    private fun isNotificationEnabled(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Android 8.0 及以上版本
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.areNotificationsEnabled()
        } else {
            // Android 7.1 及以下版本
            val appOps = getSystemService(APP_OPS_SERVICE) as AppOpsManager
            val appInfo = applicationInfo
            val packageName = applicationContext.packageName
            val uid = appInfo.uid
            try {
                val appOpsClass = Class.forName(AppOpsManager::class.java.name)
                val checkOpNoThrowMethod = appOpsClass.getMethod(
                    "checkOpNoThrow", Integer.TYPE,
                    Integer.TYPE,
                    String::class.java
                )
                val opPostNotificationValue: Field =
                    appOpsClass.getDeclaredField("OP_POST_NOTIFICATION")
                val value = opPostNotificationValue.get(Int::class.java) as Int
                (checkOpNoThrowMethod.invoke(appOps, value, uid, packageName) as Int
                        == AppOpsManager.MODE_ALLOWED)
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
                true
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
                true
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
                true
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
                true
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
                true
            }
        }
    }

    private fun getNewAppInfo() {
        UpdateChecker.check(object : UpdateChecker.Callback {
            override fun result(updateInfo: UpdateChecker.UpdateInfo?) {
                runOnUiThread {
                    updateInfo?.let {
                        if (it.buildVersionNo.toInt() > AppConfig.getVersionCode()) {
                            UpdateDialog.Builder(this@HomeActivity)
                                .setVersionName(it.buildVersion)
                                .setForceUpdate(it.needForceUpdate)
                                .setUpdateLog(it.buildUpdateDescription)
                                .setDownloadUrl(it.downloadURL)
                                .show()
                        }
                    }
                }
            }

            override fun error(message: String?) {
                Logger.d("获取系统配置失败：$message")
            }
        })
    }
    /**
     * 获取系统配置
     */
    private fun getCommonConfig() {
        EasyHttp.post(this)
            .api(GetCommonConfigApi())
            .request(object : OnHttpListener<HttpData<GetCommonConfigApi.AppConfigDto>> {
                override fun onSucceed(result: HttpData<GetCommonConfigApi.AppConfigDto>?) {
                    result?.getData()?.let {
                        AppConfig.rebateRate = it.rebateRate
                    }
                }

                override fun onFail(e: java.lang.Exception?) {

                }

            })
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        pagerAdapter?.let {
            switchFragment(it.getFragmentIndex(getSerializable(INTENT_KEY_IN_FRAGMENT_CLASS)))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewPager?.let {
            // 保存当前 Fragment 索引位置
            outState.putInt(INTENT_KEY_IN_FRAGMENT_INDEX, it.currentItem)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // 恢复当前 Fragment 索引位置
        switchFragment(savedInstanceState.getInt(INTENT_KEY_IN_FRAGMENT_INDEX))
    }

    private fun switchFragment(fragmentIndex: Int) {
        if (fragmentIndex == -1) {
            return
        }
        when (fragmentIndex) {
            0, 1, 2, 3, 4 -> {
                viewPager?.currentItem = fragmentIndex
                navigationAdapter?.setSelectedPosition(fragmentIndex)
            }
        }
    }

    /**
     * [NavigationAdapter.OnNavigationListener]
     */
    override fun onNavigationItemSelected(position: Int): Boolean {
        return when (position) {
            0, 1, 2, 3, 4 -> {
                if (position == 2) {
                    LiveDataBus.postValue("getLocation", 2)
                }
                viewPager?.currentItem = position
                true
            }

            else -> false
        }
    }

    override fun createStatusBarConfig(): ImmersionBar {
        return super.createStatusBarConfig() // 指定导航栏背景颜色
            .navigationBarColor(R.color.white)
    }

    override fun onBackPressed() {
        if (!DoubleClickHelper.isOnDoubleClick()) {
            toast(R.string.home_exit_hint)
            return
        }

        // 移动到上一个任务栈，避免侧滑引起的不良反应
        moveTaskToBack(false)
        postDelayed({
            // 进行内存优化，销毁掉所有的界面
            ActivityManager.getInstance().finishAllActivities()
        }, 300)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewPager?.adapter = null
        navigationView?.adapter = null
        navigationAdapter?.setOnNavigationListener(null)
    }

    /**
     * 获取分类数据
     */
    private fun getClassData() {
        EasyHttp.get(this)
            .api(ClassApi())
            .request(object : OnHttpListener<HttpData<ArrayList<ClassApi.ClassInfo>>> {
                override fun onSucceed(result: HttpData<ArrayList<ClassApi.ClassInfo>>?) {
                    try {
                        result?.getData()?.let {
                            AppHelper.classData = it
                            EventBus.getDefault().post(RefreshClass())
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFail(e: java.lang.Exception?) {
                    toast(e?.message)
                }
            }
            )
    }

    //显示商品信息
    @SuppressLint("SetTextI18n")
    private fun showGoodsInfoDialog(infoData: GetYouhuiApi.GoodsYouhuiDto?) {
        if (infoData == null) {
            return
        }
        val topActivity = ActivityUtils.getTopActivity()
        val inflate =
            LayoutInflater.from(topActivity).inflate(R.layout.dialog_show_goods_info, null)
        inflate.findViewById<TextView>(R.id.goods_name)?.text = infoData.title
//        inflate.findViewById<TextView>(R.id.shop_name)?.text = infoData.shop_title
        inflate.findViewById<TextView>(R.id.qhj)?.text = infoData.currentPrice
//        inflate.findViewById<TextView>(R.id.ygsy)?.text = "${infoData.fan}"
        inflate.findViewById<TextView>(R.id.xl)?.text = infoData.saleNum
        val goodsImg = inflate.findViewById<ImageView>(R.id.goods_img)
        val shopIcon = inflate.findViewById<ImageView>(R.id.shop_icon)
        val tvYhq = inflate.findViewById<TextView>(R.id.yhq_price)
        if (infoData.couponsPrice == "0" || infoData.couponsPrice == "0.0" || infoData.couponsPrice == "0.00") {
            tvYhq.visibility = View.GONE
        } else {
            tvYhq?.text = "${infoData.couponsPrice}元券"
        }
        Glide.with(topActivity).load(infoData.goodsImg).apply(
            RequestOptions().transforms(
                CenterCrop(),
                RoundedCorners(ConvertUtils.dp2px(5F))
            )
        ).into(goodsImg)
//        val options = RequestOptions()
//            .error(R.mipmap.ic_launcher)
//        Glide.with(topActivity).load(infoData.shopIcon).apply(options).into(shopIcon)

        val goodsDialog = AlertDialog.Builder(topActivity)
            .setView(inflate)
            .setCancelable(false)
            .show()
        val window = goodsDialog?.window
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent)
            val p: WindowManager.LayoutParams = window.attributes //获取对话框当前的参数值
            val screenWidth = ScreenUtils.getScreenWidth()
            p.width = (screenWidth * 0.8).toInt()
            window.attributes = p //设置生效
        }
        inflate.findViewById<TextView>(R.id.copy_kl)?.setOnClickListener {
            /*ClipboardUtils.copyText(infoData.tkl)
            ToastUtils.showShort("淘口令复制成功")*/
            goodsDialog?.dismiss()
            ClipboardUtils.clear()
        }
        inflate.findViewById<TextView>(R.id.lqyh)?.setOnClickListener {
            BrowserActivity.start(this@HomeActivity, infoData.couponLink)
            goodsDialog?.dismiss()
            ClipboardUtils.clear()
        }
        goodsDialog?.setOnDismissListener {
            Logger.d("清空剪切板")
            ClipboardUtils.clear()
        }
    }

    private fun getYouhuiInfo(contentStr: String) {
        EasyHttp.post(this)
            .api(GetYouhuiApi().apply {
                content = contentStr
            })
            .request(object : OnHttpListener<HttpData<GetYouhuiApi.GoodsYouhuiDto>> {
                override fun onSucceed(result: HttpData<GetYouhuiApi.GoodsYouhuiDto>?) {
                    result?.getData()?.let {
                        showGoodsInfoDialog(it)
                    }
                }

                override fun onFail(e: Exception?) {
//                    toast(e?.message)
                }
            })
    }

    override fun onResume() {
        super.onResume()

        getUserInfo()

        postDelayed({
            val clipboardContent = ClipboardUtils.getText()
            if (clipboardContent.isNullOrBlank().not()) {
                val toString = clipboardContent.toString()
                getYouhuiInfo(toString)
            }
        }, 2000)
    }


    /**
     * 获取登录用户信息
     */
    private fun getUserInfo() {
        if (!UserManager.loginStatus) {
            return
        }

        EasyHttp.post(this)
            .api(UserInfoApi())
            .request(object : OnHttpListener<HttpData<UserInfoApi.UserInfoDto>> {
                override fun onSucceed(result: HttpData<UserInfoApi.UserInfoDto>?) {
                    result?.getData()?.let {
                        PushAgent.getInstance(this@HomeActivity).setAlias(it.userId, "userId", null)
                        UserManager.initUserInfo(it)
                        LiveDataBus.postValue("upDateUserInfo", it)
                    }
                }

                override fun onFail(e: java.lang.Exception?) {

                }
            })
    }

}