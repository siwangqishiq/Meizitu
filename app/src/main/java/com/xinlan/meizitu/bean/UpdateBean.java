package com.xinlan.meizitu.bean;

import java.io.Serializable;

/**
 * Created by panyi on 2018/1/19.
 */

public class UpdateBean implements Serializable{
    private int version;
    private String versionString;
    private String apk;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getApk() {
        return apk;
    }

    public void setApk(String apk) {
        this.apk = apk;
    }
    public String getVersionString() {
        return versionString;
    }

    public void setVersionString(String versionString) {
        this.versionString = versionString;
    }
}//end class
