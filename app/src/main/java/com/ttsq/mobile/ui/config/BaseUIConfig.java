package com.ttsq.mobile.ui.config;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Surface;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.umeng.umverify.UMVerifyHelper;
import com.ttsq.mobile.R;


public abstract class BaseUIConfig implements AuthPageConfig {
    public Activity mActivity;
    public Context mContext;
    public UMVerifyHelper mAuthHelper;
    public int mScreenWidthDp;
    public int mScreenHeightDp;

    public static AuthPageConfig init( Activity activity, UMVerifyHelper authHelper) {
        return new FullPortConfig(activity, authHelper);
    }

    public BaseUIConfig(Activity activity, UMVerifyHelper authHelper) {
        mActivity = activity;
        mContext = activity.getApplicationContext();
        mAuthHelper = authHelper;
    }

    protected View initSwitchView(int marginTop) {
        TextView switchTV = new TextView(mContext);
        RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, ConvertUtils.dp2px( 50));
        //一键登录按钮默认marginTop 270dp
        mLayoutParams.setMargins(0, ConvertUtils.dp2px(marginTop), 0, 0);
        mLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        switchTV.setText(R.string.switch_msg);
        switchTV.setTextColor(Color.BLACK);
        switchTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13.0F);
        switchTV.setLayoutParams(mLayoutParams);
        return switchTV;
    }

    protected void updateScreenSize(int authPageScreenOrientation) {
        int screenHeightDp = ConvertUtils.px2dp( ScreenUtils.getAppScreenHeight());
        int screenWidthDp = ConvertUtils.px2dp( ScreenUtils.getAppScreenWidth());
        int rotation = mActivity.getWindowManager().getDefaultDisplay().getRotation();
        if (authPageScreenOrientation == ActivityInfo.SCREEN_ORIENTATION_BEHIND) {
            authPageScreenOrientation = mActivity.getRequestedOrientation();
        }
        if (authPageScreenOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                || authPageScreenOrientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                || authPageScreenOrientation == ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE) {
            rotation = Surface.ROTATION_90;
        } else if (authPageScreenOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                || authPageScreenOrientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                || authPageScreenOrientation == ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT) {
            rotation = Surface.ROTATION_180;
        }
        switch (rotation) {
            case Surface.ROTATION_0:
            case Surface.ROTATION_180:
                mScreenWidthDp = screenWidthDp;
                mScreenHeightDp = screenHeightDp;
                break;
            case Surface.ROTATION_90:
            case Surface.ROTATION_270:
                mScreenWidthDp = screenHeightDp;
                mScreenHeightDp = screenWidthDp;
                break;
            default:
                break;
        }
    }


    /**
     * 在横屏APP弹竖屏一键登录页面或者竖屏APP弹横屏授权页时处理特殊逻辑
     * Android8.0只能启动SCREEN_ORIENTATION_BEHIND模式的Activity
     */
    @Override
    public void onResume() {

    }

    @Override
    public void release() {
        mAuthHelper.releasePreLoginResultListener();
        mAuthHelper.setAuthListener(null);
        mAuthHelper.setUIClickListener(null);
        mAuthHelper.removeAuthRegisterViewConfig();
        mAuthHelper.removeAuthRegisterXmlConfig();
    }
}
