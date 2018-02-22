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
public class PoisitionRecordTest {
    @Before
    public void prepare(){
    }

    @Test
    public void test_hell(){
        assertEquals(100, 20*5);
    }

    @Test
    public void testPoosition() {
    }
}
