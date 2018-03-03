package com.xinlan.meizitu.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;


import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by panyi on 18-2-10.
 */

public class PositionRecord {
    private static final Logger mLogger = Logger.getLogger("PositionRecord");
    private static final String SP_KEY_POSITION = "_sp_key_position";

    private PositionRecord() {
    }

    private Map<String, Integer> records = new HashMap<String, Integer>();

    private static volatile PositionRecord mInstance;
    private static Context mContext;

    public static PositionRecord getInstance() {
        if (mInstance == null) {
            synchronized (PositionRecord.class) {
                if (mInstance == null) {
                    mInstance = new PositionRecord();
                }
            }
        }

        return mInstance;
    }

    public void init(Context context) {
        mContext = context;
        SharedPreferences sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        String rawSrc = sp.getString(SP_KEY_POSITION, "");

        if (TextUtils.isEmpty(rawSrc))
            return;

        try {
            mLogger.info("read rawSrc ---> " + rawSrc);
            records.clear();
            List<PosBean> posBeanList = JSONArray.parseArray(rawSrc, PosBean.class);
            if (posBeanList != null && posBeanList.size() == 0) {
                for (PosBean bean : posBeanList) {
                    records.put(bean.getLink(), bean.getPos());
                }//end for each
            }//end if
        } catch (Exception e) {
            mLogger.info(e.toString());
        }
    }

    public void record(final String link, final int pos) {
        if (TextUtils.isEmpty(link) || pos < 0)
            return;

        //save to memory
        records.put(link, pos);
        //save to sp
        savePosSp(link, pos);
    }

    public int getHistoryPos(final String link) {
        if (TextUtils.isEmpty(link))
            return 0;
        int ret = records.get(link) == null ? 0 : records.get(link).intValue();
        return ret >= 0 ? ret : 0;
    }

    private void savePosSp(final String link, final int pos) {
        if (TextUtils.isEmpty(link) || pos < 0)
            return;

        if (mContext == null)
            return;

        List<PosBean> recordList = new ArrayList<PosBean>(10);
        for (String key : records.keySet()) {
            if (records.get(key).intValue() > 0) {
                recordList.add(new PosBean(key, records.get(key).intValue()));
            }
        }

        String rawSrc = JSONArray.toJSONString(recordList);
        if (!TextUtils.isEmpty(rawSrc)) {
            SharedPreferences sp = mContext.getSharedPreferences(mContext.getPackageName(), Context.MODE_PRIVATE);
            sp.edit().putString(SP_KEY_POSITION, rawSrc).commit();
            mLogger.info("save ---> " + rawSrc);
        }
    }

    public static class PosBean {
        private String link;
        private int pos;

        public PosBean(String link, int pos) {
            this.link = link;
            this.pos = pos;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public int getPos() {
            return pos;
        }

        public void setPos(int pos) {
            this.pos = pos;
        }
    }//end inner class
}//end class
