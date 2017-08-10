package com.xinlan.meizitu.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.xinlan.meizitu.config.Constant;
import com.xinlan.meizitu.widget.GridSpacingItemDecoration;
import com.xinlan.meizitu.R;
import com.xinlan.meizitu.adapter.GridAdapter;
import com.xinlan.meizitu.data.Resource;
import com.xinlan.meizitu.data.Trans;
import com.xinlan.meizitu.task.FindRootNodeTask;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends BaseActivity {
    //
    private FindRootNodeTask mTask;
    private RecyclerView mGridList;

    private GridAdapter mAdapter;
    private boolean isLoadMore = false;
    private boolean isLoadEnd = false;

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

        mGridList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(isLoadMore)
                    return;

                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    GridLayoutManager lm = (GridLayoutManager) recyclerView.getLayoutManager();
                    int lastVisiblePosition = lm.findLastVisibleItemPosition();
                    if(lastVisiblePosition >= lm.getItemCount() - 1){
                        //System.out.println("Load more...");
                        loadMoreRootNode();
                    }
                }
            }
        });

        mAdapter.setItemClick(new GridAdapter.IItemClick() {
            @Override
            public void onItemClick(final int pos) {
                ImagesActivity.start(MainActivity.this, pos);
            }
        });
    }



    private void loadMoreRootNode(){
        isLoadMore = true;
        String url = Resource.getInstance().getNextPage();

        if(TextUtils.isEmpty(url)){
            isLoadMore = false;
            return;
        }
        mTask = new FindRootNodeTask();
        mTask.execute(url);
    }

    private void initAction() {
        Resource.getInstance().getRootList().clear();
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
        if(bean == null)
            return;

        switch (bean.cmd){
            case Constant.CMD_REFRESH_ROOTLIST:
                isLoadMore = false;
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
                break;

        }//end switch
    }
}//end class
