package com.xinlan.meizitu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.xinlan.meizitu.Constant;
import com.xinlan.meizitu.R;
import com.xinlan.meizitu.data.Node;
import com.xinlan.meizitu.data.Resource;
import com.xinlan.meizitu.data.Trans;
import com.xinlan.meizitu.task.ImageNodeTask;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class ImagesActivity extends BaseActivity {
    public static void start(Activity context, int pos) {
        Intent it = new Intent(context, ImagesActivity.class);
        it.putExtra(Constant.INTENT_PARAM_POS, pos);
        context.startActivity(it);
    }

    private int mPos;
    private Node mNode;
    private ImageNodeTask mImagesTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        initAction();
        initUI();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Trans bean) {
        if (bean == null)
            return;

        switch (bean.cmd) {
            case Constant.CMD_REFRESH_ROOTLIST:
                break;
        }//end switch
    }

    private void initUI() {

    }

    private void initAction() {
        mPos = getIntent().getIntExtra(Constant.INTENT_PARAM_POS, -1);
        if (mPos < 0)
            return;

        mNode = Resource.getInstance().findImageNode(mPos);
        if (mNode == null)
            return;

        if (mNode.getChildList() == null) {//load data
            mImagesTask = new ImageNodeTask();
            mImagesTask.execute(mNode.getLink());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImagesTask != null) {
            mImagesTask.cancel(true);
        }
    }
}//end class
