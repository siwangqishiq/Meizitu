package com.xinlan.meizitu.data;

import java.io.Serializable;

/**
 * Created by panyi on 2017/8/9.
 */

public class Trans implements Serializable {
    public int cmd;

    public Trans() {
    }

    public Trans(int cmd) {
        this.cmd = cmd;
    }
}//end class
