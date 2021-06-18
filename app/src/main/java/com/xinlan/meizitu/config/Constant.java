package com.xinlan.meizitu.config;

import android.os.Environment;

/**
 * Created by panyi on 2017/8/9.
 */

public class Constant {
    public static final String MEI_URL = "https://www.mzitu.com";
    //public static final String MEI_URL = "http://www.mzitu.com/page/149/";

    public static final String FOLDR_NAME = "meizitu";
    public static final String SAVE_IMAGE_PATH = Environment.
            getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()
            +"/"+FOLDR_NAME;

    public static final String SAVE_DOWNLOAD_PATH = Environment.
            getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

    public static final String UA = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31";

    public static final String INTENT_PARAM_POS = "intent_param_pos";

    public static final int CMD_REFRESH_ROOTLIST = 1;
    public static final int CMD_NODE_LIST_GET = 2;


    //存贮权限请求
    public static final int PERMISSION_REQUEST_CODE = 0x001;
}
