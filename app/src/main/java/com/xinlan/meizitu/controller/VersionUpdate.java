package com.xinlan.meizitu.controller;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.xinlan.meizitu.R;
import com.xinlan.meizitu.activity.MainActivity;
import com.xinlan.meizitu.bean.UpdateBean;
import com.xinlan.meizitu.config.Config;
import com.xinlan.meizitu.config.Constant;
import com.xinlan.meizitu.data.MessageEvent;
import com.xinlan.meizitu.data.TransCode;
import com.xinlan.meizitu.util.SystemUtil;
import com.xinlan.meizitu.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by panyi on 2018/1/19.
 */

public class VersionUpdate {
    private static Logger mLogger = Logger.getLogger("VersionUpdate");

    private volatile static VersionUpdate mVersionUpdate;

    private Handler mHandler;
    private Context mContext;
    private static boolean hasChecked = false;
    private String mApkUrl = null;

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
        mContext = context;
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
                    updateVersion(activity , updateBean.getApk());
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
    public void updateVersion(final Activity activity , final String apkUrl){
        if(TextUtils.isEmpty(apkUrl) || activity == null) {
            ToastUtil.showShort(activity , R.string.download_apk_error);
            return;
        }

        //请求存贮权限
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            this.mApkUrl = apkUrl;
            activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.PERMISSION_REQUEST_CODE);
        } else {
            doUpdateVersion(activity , apkUrl);
        }
    }

    public void onRequestPermissionsResult(final Activity activity , int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constant.PERMISSION_REQUEST_CODE &&
                grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            doUpdateVersion(activity , mApkUrl);
        }else{
            ToastUtil.showShort(activity , R.string.permission_save_request);
        }
    }

    /**
     * apk更新包文件下载成功
     * @param apkFile
     */
    protected void onDownloadSuccess(final File apkFile){
        if(apkFile == null || !apkFile.exists())
            return;
        //open apk file to install
        //System.out.println("apk === >"+apkFile.getAbsolutePath());
        ToastUtil.showLong(mContext , apkFile.getAbsolutePath());
        SystemUtil.openFile(mContext , apkFile);
    }

    private void doUpdateVersion(final Activity activity , final String apkUrl){
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle(R.string.download_apk);
        progressDialog.setMessage(activity.getString(R.string.downloading));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);

        Request downloadReq = new Request.Builder().url(apkUrl).build();
        Call call = NetClient.getClient().newCall(downloadReq);
        try{
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    handleOnDownloadError(progressDialog);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.isSuccessful()){
                        doRealDownloadApk(progressDialog , response);
                    }else{
                        handleOnDownloadError(progressDialog);
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            handleOnDownloadError(progressDialog);
        }
        progressDialog.show();
    }


    /**
     *
     * @param resp
     */
    private boolean doRealDownloadApk(final ProgressDialog progressDialog , Response resp){
        if(resp == null || !resp.isSuccessful())
            return false;


        ResponseBody body = resp.body();
        if(body == null)
            return false;

        InputStream is = null;
        byte[] buf = new byte[10*1024];
        int len = 0;
        FileOutputStream fos = null;
        File file = null;
        try {
            String savePath = isExistDir(SystemUtil.getDownloadDir());
            is = resp.body().byteStream();
            long total = resp.body().contentLength();
            file = new File(savePath, "Meizitu.apk");

            if(file.exists()){//若文件存在
                file.delete();
            }

            fos = new FileOutputStream(file);
            long sum = 0;
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
                sum += len;
                final int progress = (int) (sum * 1.0f / total * 100);
                // 下载中
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.setProgress(progress);
                    }
                });
            }
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            handleOnDownloadError(progressDialog);
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
            }
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
            }
        }

        final File finalFile = file;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                onDownloadSuccess(finalFile);
            }
        });
        return true;
    }

    private void handleOnDownloadError(final ProgressDialog progressDialog){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showShort(mContext , R.string.download_apk_error);
                if(progressDialog!=null){
                    progressDialog.cancel();
                }
            }
        });
    }

    /**
     * @param saveDir
     * @return
     * @throws IOException
     * 判断下载目录是否存在
     */
    private String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }

}//end class
