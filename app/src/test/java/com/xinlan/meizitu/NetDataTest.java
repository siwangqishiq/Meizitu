package com.xinlan.meizitu;

import com.alibaba.fastjson.JSON;
import com.xinlan.meizitu.bean.UpdateBean;
import com.xinlan.meizitu.config.Config;
import com.xinlan.meizitu.controller.NetClient;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by panyi on 18-1-20.
 */
@RunWith(JUnit4.class)
public class NetDataTest {
    private OkHttpClient okHttpClient;

    @Before
    public void prepare(){
        okHttpClient = NetClient.getClient();
    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test_hell(){
        assertEquals(100, 20*5);
    }

    @Test
    public void tesUpdateNetwork() throws IOException {
        //assertEquals(100, 20*5);
        Request req = new okhttp3.Request.Builder().url(Config.CHECK_VERSION).build();
        Call call = NetClient.mClient.newCall(req);

        Response resp = call.execute();
        assertEquals(true  , resp.isSuccessful());

        String respRaw =resp.body().string();
        assertNotNull(respRaw);
        UpdateBean updateBean = JSON.parseObject(respRaw , UpdateBean.class);
        assertEquals(true , updateBean.getVersion()>0);
        assertEquals(true , updateBean.getApk().startsWith("http"));
        assertEquals(true , updateBean.getApk().endsWith(".apk"));
    }
}
