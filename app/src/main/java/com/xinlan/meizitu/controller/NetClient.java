package com.xinlan.meizitu.controller;

import okhttp3.OkHttpClient;

/**
 * Created by panyi on 2018/1/19.
 */

public class NetClient {
    public static OkHttpClient mClient = new OkHttpClient();

    public static OkHttpClient getClient(){
        return mClient;
    }
}
