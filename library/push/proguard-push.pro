#小米
-keep class org.android.agoo.xiaomi.MiPushBroadcastReceiver {*;}

#华为
-ignorewarnings
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keep class com.huawei.hianalytics.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}

#魅族
-keep class com.meizu.cloud.** {*;}
-dontwarn com.meizu.cloud.**

#OPPO
-keep public class * extends android.app.Service

#VIVO
-dontwarn com.vivo.push.**
-keep class com.vivo.push.** {*;}
-keep class com.vivo.vms.** {*;}

#荣耀
-keep class com.hihonor.android.push.** {*;}
-keep class com.hihonor.push.** {*;}
-keep class org.android.agoo.honor.* {*;}

##友盟
-keep class com.umeng.** {*;}

-keep class org.repackage.** {*;}

-keep class com.uyumao.** { *; }

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}