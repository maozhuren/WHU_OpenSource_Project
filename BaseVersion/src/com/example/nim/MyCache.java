package com.example.nim;

import android.content.Context;

import com.netease.nimlib.sdk.StatusBarNotificationConfig;

public class MyCache {
    private static Context context;

    private static String account;

    private static StatusBarNotificationConfig config;

    public static void clear() {
        account = null;
    }

    public static String getAccount() {
        return account;
    }

    public static void setAccount(String account) {
        MyCache.account = account;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        MyCache.context = context.getApplicationContext();
    }

    public static StatusBarNotificationConfig getConfig() {
        return config;
    }

    public static void setConfig(StatusBarNotificationConfig config) {
        MyCache.config = config;
    }
}
