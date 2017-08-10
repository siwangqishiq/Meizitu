package com.xinlan.meizitu.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.xinlan.meizitu.R;
import com.xinlan.meizitu.util.ImageUtil;
import com.xinlan.meizitu.widget.PinchImageView;

/**
 * Created by panyi on 2017/8/10.
 */

public class ImageFragment extends Fragment {
    private String image;
    private String refer;

    private View mRootView;
    private PinchImageView mImageView;
    private ProgressBar mProgressBar;

    public static ImageFragment newInstance(String img, String path) {
        ImageFragment frg = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("image", img);
        bundle.putString("refer", path);
        frg.setArguments(bundle);
        return frg;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() == null)
            return;

        image = getArguments().getString("image");
        refer = getArguments().getString("refer");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.view_gallery_image, container, false);
        mImageView = (PinchImageView) mRootView.findViewById(R.id.img);
        mProgressBar = (ProgressBar) mRootView.findViewById(R.id.progress);
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageUtil.loadImage(this,refer,image,mImageView,mProgressBar);
    }
}//end class
