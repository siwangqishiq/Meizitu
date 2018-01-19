package com.xinlan.meizitu.data;

import java.io.Serializable;

/**
 * Created by panyi on 2018/1/19.
 */

public class MessageEvent {
    private int cmd;
    private Serializable data;
    private String msg;

    public MessageEvent(int cmd, Serializable data, String msg) {
        this.cmd = cmd;
        this.data = data;
        this.msg = msg;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public Serializable getData() {
        return data;
    }

    public void setData(Serializable data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
