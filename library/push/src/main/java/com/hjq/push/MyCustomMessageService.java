package com.hjq.push;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengMessageService;
import com.umeng.message.entity.UMessage;

import org.android.agoo.common.AgooConstants;
import org.json.JSONObject;

/**
 * 自定义接收并处理友盟推送消息的服务类
 */
public class MyCustomMessageService extends UmengMessageService {
    private static final String TAG = "MyCustomMessageService";

    @Override
    public void onMessage(Context context, Intent intent) {
        Log.i(TAG, "onMessage");
        try {
            String body = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
            UMessage message = new UMessage(new JSONObject(body));
            if (UMessage.DISPLAY_TYPE_NOTIFICATION.equals(message.display_type)) {
                //处理通知消息
                handleNotificationMessage(message);
            } else if (UMessage.DISPLAY_TYPE_CUSTOM.equals(message.display_type)) {
                //TODO: 开发者实现：处理自定义透传消息
                handleCustomMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleCustomMessage(UMessage message) {
        Log.i(TAG, "handleCustomMessage: " + message.getRaw().toString());
    }

    @TargetApi(Build.VERSION_CODES.O)
    private static NotificationChannel getDefaultMode(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = manager.getNotificationChannel("custom");
        if (channel != null) {
            return channel;
        }
        channel = new NotificationChannel("custom", "MyCustom", NotificationManager.IMPORTANCE_HIGH);
        int raw = R.raw.umeng_push_notification_default_sound;
        Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/raw/" + raw);
        channel.setSound(uri, Notification.AUDIO_ATTRIBUTES_DEFAULT);
        channel.setShowBadge(true);
        channel.enableVibration(true);
        manager.createNotificationChannel(channel);
        return channel;
    }

    private void handleNotificationMessage(UMessage msg) {
        Notification.Builder builder;
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = getDefaultMode(this);
            builder = new Notification.Builder(this, channel.getId());
        } else {
            builder = new Notification.Builder(this);
        }
        builder.setContentTitle(msg.title)
                .setContentText(msg.text)
                .setTicker(msg.ticker)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.umeng_push_notification_default_small_icon)
                .setAutoCancel(true);
        Notification notification = builder.getNotification();
        PendingIntent clickIntent = getClickPendingIntent(this, msg);
        notification.deleteIntent = getDismissPendingIntent(this, msg);
        notification.contentIntent = clickIntent;
        manager.notify((int) SystemClock.elapsedRealtime(), notification);
        UTrack.getInstance().trackMsgShow(msg, notification);
    }

    public PendingIntent getClickPendingIntent(Context context, UMessage msg) {
        Intent intent = new Intent(context, MyCustomNotificationClickActivity.class);
        intent.setPackage(context.getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(MyCustomNotificationClickActivity.EXTRA_BODY, msg.getRaw().toString());
        int flags = PendingIntent.FLAG_CANCEL_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }
        return PendingIntent.getActivity(context, (int) (System.currentTimeMillis()), intent, flags);
    }

    public PendingIntent getDismissPendingIntent(Context context, UMessage msg) {
        return new UmengMessageHandler().getDismissPendingIntent(context, msg);
    }

}
