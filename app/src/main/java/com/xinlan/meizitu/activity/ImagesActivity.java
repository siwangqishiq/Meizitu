package com.xinlan.meizitu.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.xinlan.meizitu.config.Constant;
import com.xinlan.meizitu.R;
import com.xinlan.meizitu.data.Node;
import com.xinlan.meizitu.data.Resource;
import com.xinlan.meizitu.data.Trans;
import com.xinlan.meizitu.fragment.ImageFragment;
import com.xinlan.meizitu.task.ImageNodeTask;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ImagesActivity extends BaseActivity {
    public static void start(Activity context, int pos) {
        Intent it = new Intent(context, ImagesActivity.class);
        it.putExtra(Constant.INTENT_PARAM_POS, pos);
        context.startActivity(it);
    }

    private int mPos;
    private Node mNode;
    private ImageNodeTask mImagesTask;

    private ViewPager mGallery;
    private ImagesAdapter mAdapter;
    private TextView mPageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_images);
        initUI();
        initAction();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Trans bean) {
        if (bean == null)
            return;

        switch (bean.cmd) {
            case Constant.CMD_NODE_LIST_GET:
                mNode = Resource.getInstance().findImageNode(mPos);
                if(mNode.getChildList() == null){
                    Toast.makeText(this,R.string.not_get_data,Toast.LENGTH_SHORT).show();
                    return;
                }
                refreshImageList();
                break;
        }//end switch
    }

    private void initUI() {
        mGallery = (ViewPager)findViewById(R.id.gallery);
        mPageText = (TextView)findViewById(R.id.pages_text);
    }

    private void refreshImageList(){
        if(mNode.getChildList() == null){
            return;
        }

        mAdapter = new ImagesAdapter(this.getSupportFragmentManager());
        mGallery.setAdapter(mAdapter);

        final int total = mNode.getChildList().size();

        mGallery.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPageText.setVisibility(View.VISIBLE);
                mPageText.setText(String.format("%s/%s",position+1,total));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mPageText.setVisibility(View.VISIBLE);
        mPageText.setText(String.format("%s/%s",1,total));
    }

    private void initAction() {
        mPos = getIntent().getIntExtra(Constant.INTENT_PARAM_POS, -1);
        if (mPos < 0)
            return;

        mNode = Resource.getInstance().findImageNode(mPos);
        if (mNode == null)
            return;

        if (mNode.getChildList() == null) {//load data
            mPageText.setVisibility(View.INVISIBLE);
            mImagesTask = new ImageNodeTask(mNode);
            mImagesTask.execute(mNode.getLink());
        }else{
            refreshImageList();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImagesTask != null) {
            mImagesTask.cancel(true);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
//        }
    }

    private final class ImagesAdapter extends FragmentStatePagerAdapter{
        public ImagesAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Node n = mNode.getChildList().get(position);
            return ImageFragment.newInstance(n.getImage(),n.getRefer());
        }

        @Override
        public int getCount() {
            return mNode.getChildList().size();
        }
    }
}//end class
