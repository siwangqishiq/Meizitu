package com.xinlan.meizitu.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;

import com.xinlan.meizitu.Constant;
import com.xinlan.meizitu.GridSpacingItemDecoration;
import com.xinlan.meizitu.R;
import com.xinlan.meizitu.adapter.GridAdapter;
import com.xinlan.meizitu.data.Trans;
import com.xinlan.meizitu.task.FindRootNodeTask;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends BaseActivity {
    //
    private FindRootNodeTask mTask;
    private RecyclerView mGridList;

    private GridAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        initAction();
    }

    private void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGridList = (RecyclerView) findViewById(R.id.grid_list);
//        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        mGridList.setLayoutManager(layoutManager);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this , 2);
        mGridList.setLayoutManager(layoutManager);
        mAdapter = new GridAdapter();
        mGridList.setAdapter(mAdapter);
        mGridList.addItemDecoration(new GridSpacingItemDecoration(2, 10, true));
        mGridList.setItemAnimator(new DefaultItemAnimator());
        mGridList.setHasFixedSize(true);

        
    }

    private void initAction() {
        mTask = new FindRootNodeTask();
        mTask.execute(Constant.MEI_URL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTask != null) {
            mTask.cancel(true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Trans bean) {
        if (bean.cmd == Constant.CMD_REFRESH_ROOTLIST) {
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}//end class
