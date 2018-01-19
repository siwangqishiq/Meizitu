package com.xinlan.meizitu.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by panyi on 2018/1/19.
 */

public final class SystemUtil {
    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public static int getVersionCode(final Context context) {
        if(context == null)
            return -1;

        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            return 0;
        }
    }
}
