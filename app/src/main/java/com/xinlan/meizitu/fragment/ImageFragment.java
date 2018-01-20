package com.xinlan.meizitu.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.xinlan.meizitu.R;
import com.xinlan.meizitu.config.Constant;
import com.xinlan.meizitu.util.ImageUtil;
import com.xinlan.meizitu.util.ThreadPoolTask;
import com.xinlan.meizitu.util.ToastUtil;
import com.xinlan.meizitu.widget.PinchImageView;

import java.util.concurrent.ExecutionException;

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
        mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDialog();
                return true;
            }
        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(new String[]{"保存图片"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int index) {
                switch (index){
                    case 0:
                        saveImageToLocal();
                        break;
                }
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.PERMISSION_REQUEST_CODE) {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                doSaveImageToLocal();
            }
        }
    }

    /**
    * 保存图片到本地
     */
    private void saveImageToLocal(){
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.PERMISSION_REQUEST_CODE);
        } else {
            doSaveImageToLocal();
        }
    }

    private void doSaveImageToLocal(){
        if(TextUtils.isEmpty(image)){
            ToastUtil.showShort(getContext(),R.string.save_image_error);
            return;
        }

        ThreadPoolTask.submitTask(new Runnable() {
            @Override
            public void run() {
                RequestOptions requestOptions = new RequestOptions();
                Bitmap bitmap = null;
                try {
                    bitmap =  Glide.with(getContext()).asBitmap().load(image).submit(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    bitmap = null;
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    bitmap = null;
                }

                if(bitmap != null &&  ImageUtil.saveImageToGallery(getContext(),bitmap, Constant.SAVE_IMAGE_PATH)){
                    mImageView.post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShort(getContext(),R.string.save_image_success);
                        }
                    });
                }else{
                    mImageView.post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShort(getContext(),R.string.save_image_error);
                        }
                    });
                }//end if
            }
        });
    }
}//end class
