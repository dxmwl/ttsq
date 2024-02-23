package com.ttsq.mobile.ui.config;

public interface AuthPageConfig {

    /**
     * 配置授权页样式
     */
    void configAuthPage();

    /**
     * android8.0兼容
     */
    void onResume();

    /**
     * 释放sdk内部引用，防止内存泄漏
     */
    void release();
}
