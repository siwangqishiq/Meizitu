package com.xinlan.meizitu.controller;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.xinlan.meizitu.R;
import com.xinlan.meizitu.activity.MainActivity;
import com.xinlan.meizitu.bean.UpdateBean;
import com.xinlan.meizitu.config.Config;
import com.xinlan.meizitu.data.MessageEvent;
import com.xinlan.meizitu.data.TransCode;
import com.xinlan.meizitu.util.SystemUtil;

import org.greenrobot.eventbus.EventBus;

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
                        mLogger.info("最新版本 = "+updateBean.getVersion() +"  当前版本 = "+ SystemUtil.getVersionCode(context));
                        mLogger.info("最新版本:"+updateBean.getVersionString());
                        mLogger.info("下载地址:"+updateBean.getApk());
                        MessageEvent event = new MessageEvent(TransCode.CMD_UPDATE , updateBean ,null);
                        EventBus.getDefault().post(event);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void handleOnUpdate(final Activity activity , final UpdateBean updateBean){
        if(activity == null)
            return;

        int currentVersion = SystemUtil.getVersionCode(activity);

        if(currentVersion < updateBean.getVersion() && !TextUtils.isEmpty(updateBean.getApk())){//弹出更新对话框
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
            alertDialogBuilder.setMessage(R.string.update_msg)
                    .setCancelable(false).setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    doUpdateVersion();
                }
            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    /**
     * 下载apk 更新操作
     */
    public void doUpdateVersion(){

    }
}//end class
