package com.whx.baselib.utils;

import android.content.pm.ApplicationInfo;

import com.whx.baselib.RuntimeInfo;


/**
 * Created by zdx on 2020/5/7.
 * Description:
 * Update:
 **/
public class BasicConfig {
    private static final BasicConfig ourInstance = new BasicConfig();

    public static BasicConfig getInstance() {
        return ourInstance;
    }

    private BasicConfig() {
    }

    /**
     * 判断当前应用是否是debug状态
     */
    public static boolean isDebuggable() {
        try {
            ApplicationInfo info = RuntimeInfo.INSTANCE.getSApplicationContext().getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
