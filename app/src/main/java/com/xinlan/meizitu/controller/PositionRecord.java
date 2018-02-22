package com.xinlan.meizitu.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by panyi on 18-2-10.
 */

public class PositionRecord {
    private static final Logger mLogger = Logger.getLogger("PositionRecord");
    private static final String SP_KEY_POSITION = "_sp_key_position";

    private PositionRecord(){
    }

    private Map<String,Integer> records = new HashMap<String,Integer>();

    private static volatile PositionRecord mInstance;

    public PositionRecord getInstance(){
        if(mInstance == null){
            synchronized (PositionRecord.class){
                if(mInstance == null){
                    mInstance = new PositionRecord();
                }
            }
        }

        return mInstance;
    }

    public void init(Context context){
        SharedPreferences sp = context.getSharedPreferences(context.getPackageName() , Context.MODE_PRIVATE);
        String rawSrc = sp.getString(SP_KEY_POSITION , SP_KEY_POSITION);

        if(TextUtils.isEmpty(rawSrc))
            return;

        try{
            records.clear();
            
        }catch (Exception e){
            mLogger.info(e.toString());
        }
    }
}//end class
