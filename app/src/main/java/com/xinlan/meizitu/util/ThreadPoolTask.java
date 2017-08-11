package com.xinlan.meizitu.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 潘易 on 2017/8/12.
 */

public class ThreadPoolTask {
    public static ExecutorService comThreadPool = Executors.newSingleThreadExecutor();
    public static void submitTask(final Runnable runnable){
        comThreadPool.submit(runnable);
    }
}
