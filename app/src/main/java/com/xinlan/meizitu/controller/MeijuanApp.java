package com.xinlan.meizitu.controller;

import android.app.Application;

/**
 * Created by panyi on 2018/1/19.
 */

public class MeijuanApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VersionUpdate.getInstance().init(this);//更新检测控件
        PositionRecord.getInstance().init(this);//位置记录控件初始化
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}
