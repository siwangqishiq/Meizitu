package com.xinlan.meizitu.task;

import android.os.AsyncTask;

import com.xinlan.meizitu.Constant;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by panyi on 2017/8/9.
 */

public class ImageNodeTask extends AsyncTask<String, Void, Integer> {

    @Override
    protected Integer doInBackground(String... strings) {
        String url = strings[0];
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent(Constant.UA).get();
            System.out.println(doc.body().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
    }

}//end class
