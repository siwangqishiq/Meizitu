package com.xinlan.meizitu.controller;

import android.content.Context;
import android.os.Handler;

import com.alibaba.fastjson.JSON;
import com.xinlan.meizitu.bean.UpdateBean;
import com.xinlan.meizitu.config.Config;
import com.xinlan.meizitu.util.SystemUtil;

import java.io.IOException;
import java.util.logging.Logger;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by panyi on 2018/1/19.
 */

public class VersionUpdate {
    private static Logger mLogger = Logger.getLogger("VersionUpdate");

    private volatile static VersionUpdate mVersionUpdate;

    private Handler mHandler;
    private static boolean hasChecked = false;

    private VersionUpdate(){
        hasChecked = false;
    }

    public static VersionUpdate getInstance(){
        if(mVersionUpdate == null){
            synchronized (VersionUpdate.class){
                if(mVersionUpdate == null){
                    mVersionUpdate = new VersionUpdate();
                }
            }
        }
        return mVersionUpdate;
    }

    public void init(final Context context){
        mHandler = new Handler(context.getMainLooper());
    }

    public void checkNewVersion(final Context context){
        if(hasChecked)
            return;

        hasChecked = true;
        Request req = new Request.Builder().url(Config.CHECK_VERSION).build();
        Call call = NetClient.mClient.newCall(req);
        call.enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                //do nothing
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    //
                    try {
                        String respRaw = response.body().string();
                        mLogger.info(respRaw);
                        UpdateBean updateBean = JSON.parseObject(respRaw , UpdateBean.class);
                        System.out.println("最新版本 = "+updateBean.getVersion() +"  当前版本 = "+ SystemUtil.getVersionCode(context));
                        System.out.println("最新版本:"+updateBean.getVersionString());
                        System.out.println("下载地址:"+updateBean.getApk());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}//end class
