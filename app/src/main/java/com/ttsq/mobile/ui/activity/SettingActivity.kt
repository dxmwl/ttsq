package com.ttsq.mobile.ui.activity

import android.view.Gravity
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.SPUtils
import com.hjq.base.BaseDialog
import com.hjq.base.action.AnimAction
import com.ttsq.mobile.R
import com.ttsq.mobile.aop.SingleClick
import com.ttsq.mobile.app.AppActivity
import com.ttsq.mobile.http.api.LogoutApi
import com.ttsq.mobile.http.glide.GlideApp
import com.ttsq.mobile.http.model.HttpData
import com.ttsq.mobile.manager.ActivityManager
import com.ttsq.mobile.manager.CacheDataManager
import com.ttsq.mobile.other.AppConfig
import com.ttsq.mobile.ui.dialog.MenuDialog
import com.ttsq.mobile.ui.dialog.SafeDialog
import com.ttsq.mobile.ui.dialog.UpdateDialog
import com.hjq.http.EasyHttp
import com.hjq.http.listener.HttpCallback
import com.hjq.widget.layout.SettingBar
import com.hjq.widget.view.SwitchButton
import com.ttsq.mobile.app.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2019/03/01
 *    desc   : 设置界面
 */
class SettingActivity : AppActivity(), SwitchButton.OnCheckedChangeListener {

    private val languageView: SettingBar? by lazy { findViewById(R.id.sb_setting_language) }
    private val phoneView: SettingBar? by lazy { findViewById(R.id.sb_setting_phone) }
    private val passwordView: SettingBar? by lazy { findViewById(R.id.sb_setting_password) }
    private val cleanCacheView: SettingBar? by lazy { findViewById(R.id.sb_setting_cache) }
    private val autoSwitchView: SwitchButton? by lazy { findViewById(R.id.sb_setting_switch) }
    private val gxtj_switch: SwitchButton? by lazy { findViewById(R.id.gxtj_switch) }

    override fun getLayoutId(): Int {
        return R.layout.setting_activity
    }

    override fun initView() {
        // 设置切换按钮的监听
        autoSwitchView?.setOnCheckedChangeListener(this)
        gxtj_switch?.setOnCheckedChangeListener(this)
        setOnClickListener(
            R.id.sb_setting_language, R.id.sb_setting_update, R.id.sb_setting_phone,
            R.id.sb_setting_password, R.id.sb_setting_agreement, R.id.sb_setting_about,
            R.id.sb_setting_cache, R.id.sb_setting_exit, R.id.user_xieyi
        )

        autoSwitchView?.setChecked(
            SPUtils.getInstance("APP_CONFIG").getBoolean("PUSH_SWITCH", true)
        )
        gxtj_switch?.setChecked(
            SPUtils.getInstance("APP_CONFIG").getBoolean("GXTJ_SWITCH", true)
        )
    }

    override fun initData() {
        // 获取应用缓存大小
        cleanCacheView?.setRightText(CacheDataManager.getTotalCacheSize(this))
        languageView?.setRightText("简体中文")
        phoneView?.setRightText("181****1413")
        passwordView?.setRightText("密码强度较低")
    }

    @SingleClick
    override fun onClick(view: View) {
        when (view.id) {
            R.id.user_xieyi -> {
                BrowserActivity.start(this, Constants.URL_USER_AGREEMENT)
            }
            R.id.sb_setting_language -> {

                // 底部选择框
                MenuDialog.Builder(this) // 设置点击按钮后不关闭对话框
                    //.setAutoDismiss(false)
                    .setList(R.string.setting_language_simple, R.string.setting_language_complex)
                    .setListener(object : MenuDialog.OnListener<String> {

                        override fun onSelected(dialog: BaseDialog?, position: Int, data: String) {
                            languageView?.setRightText(data)
                            BrowserActivity.start(
                                this@SettingActivity,
                                "https://github.com/getActivity/MultiLanguages"
                            )
                        }
                    })
                    .setGravity(Gravity.BOTTOM)
                    .setAnimStyle(AnimAction.ANIM_BOTTOM)
                    .show()
            }
            R.id.sb_setting_update -> {

                // 本地的版本码和服务器的进行比较
                if (20 > AppConfig.getVersionCode()) {
                    UpdateDialog.Builder(this)
                        .setVersionName("2.0")
                        .setForceUpdate(false)
                        .setUpdateLog("修复Bug\n优化用户体验")
                        .setDownloadUrl("https://down.qq.com/qqweb/QQ_1/android_apk/Android_8.5.0.5025_537066738.apk")
                        .setFileMd5("560017dc94e8f9b65f4ca997c7feb326")
                        .show()
                } else {
                    toast(R.string.update_no_update)
                }
            }
            R.id.sb_setting_phone -> {

                SafeDialog.Builder(this)
                    .setListener(object : SafeDialog.OnListener {

                        override fun onConfirm(dialog: BaseDialog?, phone: String, code: String) {
                            PhoneResetActivity.start(this@SettingActivity, code)
                        }
                    })
                    .show()
            }
            R.id.sb_setting_password -> {

                SafeDialog.Builder(this)
                    .setListener(object : SafeDialog.OnListener {

                        override fun onConfirm(dialog: BaseDialog?, phone: String, code: String) {
                            PasswordResetActivity.start(this@SettingActivity, phone, code)
                        }
                    })
                    .show()
            }
            R.id.sb_setting_agreement -> {

                BrowserActivity.start(this, Constants.URL_PRIVACY_POLICY)
            }
            R.id.sb_setting_about -> {

                startActivity(AboutActivity::class.java)
            }
            R.id.sb_setting_cache -> {

                // 清除内存缓存（必须在主线程）
                GlideApp.get(this@SettingActivity).clearMemory()
                lifecycleScope.launch(Dispatchers.IO) {
                    CacheDataManager.clearAllCache(this@SettingActivity)
                    // 清除本地缓存（必须在子线程）
                    GlideApp.get(this@SettingActivity).clearDiskCache()
                    withContext(Dispatchers.Main) {
                        // 重新获取应用缓存大小
                        cleanCacheView?.setRightText(CacheDataManager.getTotalCacheSize(this@SettingActivity))
                    }
                }
            }
            R.id.sb_setting_exit -> {

                if (true) {
                    startActivity(LoginActivity::class.java)
                    // 进行内存优化，销毁除登录页之外的所有界面
                    ActivityManager.getInstance().finishAllActivities(
                        LoginActivity::class.java
                    )
                    return
                }

                // 退出登录
                EasyHttp.post(this)
                    .api(LogoutApi())
                    .request(object : HttpCallback<HttpData<Void?>>(this) {

                        override fun onSucceed(data: HttpData<Void?>?) {
                            startActivity(LoginActivity::class.java)
                            // 进行内存优化，销毁除登录页之外的所有界面
                            ActivityManager.getInstance()
                                .finishAllActivities(LoginActivity::class.java)
                        }
                    })
            }
        }
    }

    /**
     * [SwitchButton.OnCheckedChangeListener]
     */
    override fun onCheckedChanged(button: SwitchButton, checked: Boolean) {
        when (button) {
            gxtj_switch -> {
                SPUtils.getInstance("APP_CONFIG").put("GXTJ_SWITCH", gxtj_switch?.isChecked() == true)
            }
            autoSwitchView->{
                SPUtils.getInstance("APP_CONFIG").put("PUSH_SWITCH", autoSwitchView?.isChecked() == true)
            }
            else -> {}
        }
    }
}