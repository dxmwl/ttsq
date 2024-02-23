package com.hjq.push;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.umeng.message.UTrack;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import org.json.JSONObject;

/**
 * 点击通知栏消息后，落地的Activity类
 */
public class MyCustomNotificationClickActivity extends Activity {

    public static final String EXTRA_BODY = "body";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            String body = intent.getStringExtra(EXTRA_BODY);
            if (body != null) {
                try {
                    UMessage message = new UMessage(new JSONObject(body));
                    UTrack.getInstance().trackMsgClick(message);

                    //TODO: 开发者实现：处理通知点击消息
                    new UmengNotificationClickHandler().handleMessage(this, message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        finish();
    }

}
