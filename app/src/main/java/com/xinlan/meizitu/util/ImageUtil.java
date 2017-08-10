package com.xinlan.meizitu.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.xinlan.meizitu.R;

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
        TransitionOptions transOpt = new GenericTransitionOptions();
        Glide.with(context).load(gliderUrl).transition(transOpt).apply(options).into(img);
    }

    public static void loadImage(Fragment context, final String refer, final String url, final ImageView img, final ProgressBar progress) {
        RequestOptions options = new RequestOptions().error(R.drawable.error).fitCenter();
        GlideUrl gliderUrl = new GlideUrl(url, new Headers() {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> header = new HashMap<>();
                header.put("Referer", refer);
                return header;
            }
        });
        Glide.with(context).load(gliderUrl).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                progress.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progress.setVisibility(View.GONE);
                return false;
            }
        }).apply(options).into(img);
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
