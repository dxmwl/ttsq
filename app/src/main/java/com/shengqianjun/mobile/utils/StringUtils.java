package com.shengqianjun.mobile.utils;

import android.text.TextUtils;

/**
 * @author : clb
 * @project : EasyBuy_Android
 * @Description : 项目描述
 * @time : 2022/6/23
 */
public class StringUtils {

    public static String changeParamForKey(String url, String key, String value) {
        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(key)) {
            url = url.replaceAll("(" + key + "=[^&]*)", key + "=" + value);
        }
        return url;
    }
}
