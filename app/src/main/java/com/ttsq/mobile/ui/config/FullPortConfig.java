package com.ttsq.mobile.ui.config;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.umeng.umverify.UMResultCode;
import com.umeng.umverify.UMVerifyHelper;
import com.umeng.umverify.listener.UMAuthUIControlClickListener;
import com.umeng.umverify.view.UMAuthUIConfig;
import com.ttsq.mobile.R;
import com.ttsq.mobile.other.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

public class FullPortConfig extends BaseUIConfig {
    private final String TAG = "全屏竖屏样式";

    public FullPortConfig(Activity activity, UMVerifyHelper authHelper) {
        super(activity, authHelper);
    }

    @Override
    public void configAuthPage() {
        mAuthHelper.setUIClickListener(new UMAuthUIControlClickListener() {
            @Override
            public void onClick(String code, Context context, String jsonString) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonString);
                    switch (code) {
                        //点击授权页默认样式的返回按钮
                        case UMResultCode.CODE_ERROR_USER_CANCEL:
                            Log.e(TAG, "点击了授权页默认返回按钮");
                            mAuthHelper.quitLoginPage();
                            mActivity.finish();
                            break;
                        //点击授权页默认样式的切换其他登录方式 会关闭授权页
                        //如果不希望关闭授权页那就setSwitchAccHidden(true)隐藏默认的  通过自定义view添加自己的
                        case UMResultCode.CODE_ERROR_USER_SWITCH:
                            Log.e(TAG, "点击了授权页默认切换其他登录方式");
                            break;
                        //点击一键登录按钮会发出此回调
                        //当协议栏没有勾选时 点击按钮会有默认toast 如果不需要或者希望自定义内容 setLogBtnToastHidden(true)隐藏默认Toast
                        //通过此回调自己设置toast
                        case UMResultCode.CODE_ERROR_USER_LOGIN_BTN:
                            if (!jsonObj.getBoolean("isChecked")) {
                                Toast.makeText(mContext, R.string.custom_toast, Toast.LENGTH_SHORT).show();
                            }
                            break;
                        //checkbox状态改变触发此回调
                        case UMResultCode.CODE_ERROR_USER_CHECKBOX:
                            Log.e(TAG, "checkbox状态变为" + jsonObj.getBoolean("isChecked"));
                            break;
                        //点击协议栏触发此回调
                        case UMResultCode.CODE_ERROR_USER_PROTOCOL_CONTROL:
                            Log.e(TAG, "点击协议，" + "name: " + jsonObj.getString("name") + ", url: " + jsonObj.getString("url"));
                            break;
                        default:
                            break;

                    }
                } catch (JSONException e) {

                }
            }
        });
        mAuthHelper.removeAuthRegisterXmlConfig();
        mAuthHelper.removeAuthRegisterViewConfig();
//        //添加自定义切换其他登录方式
//        mAuthHelper.addAuthRegistViewConfig("switch_msg", new UMAuthRegisterViewConfig.Builder()
//                .setView(initSwitchView(350))
//                .setRootViewId(UMAuthRegisterViewConfig.RootViewId.ROOT_VIEW_ID_BODY)
//                .setCustomInterface(new UMCustomInterface() {
//                    @Override
//                    public void onClick(Context context) {
//                        Toast.makeText(mContext, "切换到短信登录方式", Toast.LENGTH_SHORT).show();
//                        Intent pIntent = new Intent(mActivity, MessageActivity.class);
//                        mActivity.startActivityForResult(pIntent, 1002);
//                        mAuthHelper.quitLoginPage();
//                    }
//                }).build());
        int authPageOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;
        if (Build.VERSION.SDK_INT == 26) {
            authPageOrientation = ActivityInfo.SCREEN_ORIENTATION_BEHIND;
        }

        UMAuthUIConfig.Builder builder = new UMAuthUIConfig.Builder()
                .setAppPrivacyOne("《用户协议》", AppConfig.INSTANCE.getUserAgreementUrl())
                .setAppPrivacyTwo("《隐私政策》", AppConfig.INSTANCE.getPrivacyPolicyUrl())
                .setAppPrivacyColor(Color.GRAY, ContextCompat.getColor(mContext,R.color.primary_color))
                //隐藏默认切换其他登录方式
                .setSwitchAccHidden(true)
                //隐藏默认Toast
                .setLogBtnToastHidden(true)
                //沉浸式状态栏
                .setStatusBarColor(Color.TRANSPARENT)
                .setStatusBarUIFlag(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
                //隐藏默认导航
                .setNavHidden(true)
                .setLightColor(true)
                .setWebNavTextSizeDp(20)
                //图片或者xml的传参方式为不包含后缀名的全称 需要文件需要放在drawable或drawable-xxx目录下 in_activity.xml, mytel_app_launcher.png
                .setAuthPageActIn("in_activity", "out_activity")
                .setAuthPageActOut("in_activity", "out_activity")
                .setVendorPrivacyPrefix("《")
                .setVendorPrivacySuffix("》")
                .setPageBackgroundPath("page_background_color")
//                .setLogoImgPath("mytel_app_launcher")
                //一键登录按钮三种状态背景示例login_btn_bg.xml
                .setLogBtnBackgroundPath("login_btn_bg")
                //设置二次隐私协议弹窗是否需要显示
//                .setPrivacyAlertIsNeedShow(true)
//                .setPrivacyAlertIsNeedAutoLogin(true)
//                .setPrivacyAlertMaskIsNeedShow(true)
                .setScreenOrientation(authPageOrientation);
        Drawable logoDrawable = ContextCompat.getDrawable(mContext,R.mipmap.ic_launcher);
        builder.setLogoImgDrawable(logoDrawable);
        mAuthHelper.setAuthUIConfig(builder.create());
    }


}
