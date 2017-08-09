package com.xinlan.meizitu;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.bumptech.glide.request.RequestOptions;

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

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.loading).centerCrop();

        GlideUrl gliderUrl = new GlideUrl(url, headers);
        Glide.with(context).load(gliderUrl).apply(options).into(img);
    }

    /**
     * Glide
     .with( context )
     .load( eatFoodyImages[0] )
     .skipMemoryCache( true )
     .into( imageViewInternet );
     */


    /**
     * Glide
     .with( context )
     .load( eatFoodyImages[0] )
     .diskCacheStrategy( DiskCacheStrategy.NONE )
     .into( imageViewInternet );
     */
}
