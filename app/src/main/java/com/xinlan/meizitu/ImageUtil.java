package com.xinlan.meizitu;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by panyi on 2017/8/9.
 */

public class ImageUtil {

    public static void loadImage(Context context, final String refer, final String url, final ImageView img) {
        Headers headers = new Headers() {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> header = new HashMap<>();
                header.put("Referer", refer);
                return header;
            }
        };

        GlideUrl gliderUrl = new GlideUrl(url, headers);
        Glide.with(context).load(gliderUrl).into(img);
    }
}
