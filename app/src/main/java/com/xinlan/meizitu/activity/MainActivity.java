package com.xinlan.meizitu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

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
    private SwipeRefreshLayout mRefreshLayout;

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
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
//        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        mGridList.setLayoutManager(layoutManager);
    }


    private void loadMoreRootNode() {
        isLoadMore = true;
        String url = Resource.getInstance().getNextPage();

        if (TextUtils.isEmpty(url)) {
            isLoadMore = false;
            return;
        }
        mTask = new FindRootNodeTask();
        mTask.execute(url);
    }

    private void initAction() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        mGridList.setLayoutManager(layoutManager);

        //
        Resource.getInstance().getRootList().clear();
        //System.out.println("--->"+Resource.getInstance().getRootList().size());

        mAdapter = new GridAdapter();
        mAdapter.notifyDataSetChanged();
        mGridList.setAdapter(mAdapter);

        mGridList.addItemDecoration(new GridSpacingItemDecoration(2, 10, true));
        mGridList.setItemAnimator(new DefaultItemAnimator());
        mGridList.setHasFixedSize(true);

        mGridList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (isLoadMore)
                    return;

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    GridLayoutManager lm = (GridLayoutManager) recyclerView.getLayoutManager();
                    int lastVisiblePosition = lm.findLastVisibleItemPosition();
                    if (lastVisiblePosition >= lm.getItemCount() - 1) {
                        //System.out.println("Load more...");
                        loadMoreRootNode();
                    }
                }
            }
        });

        mAdapter.setItemClick(new GridAdapter.IItemClick() {
            @Override
            public void onItemClick(final View view, final int pos) {
                ActivityOptionsCompat optionCompat = ActivityOptionsCompat.makeScaleUpAnimation(view,
                        view.getWidth() / 2, view.getHeight() / 2, 0, 0);
                Intent it = new Intent(MainActivity.this, ImagesActivity.class);
                it.putExtra(Constant.INTENT_PARAM_POS, pos);
                ActivityCompat.startActivity(MainActivity.this, it, optionCompat.toBundle());
            }
        });

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        loadData();
    }

    private void loadData() {
        isLoadEnd = false;
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
        if (bean == null)
            return;

        switch (bean.cmd) {
            case Constant.CMD_REFRESH_ROOTLIST:
                mRefreshLayout.setRefreshing(false);
                isLoadMore = false;
                //System.out.println("get cmd");
                if (mAdapter != null) {
                    Resource.LastRecord record = Resource.getInstance().getLstRecord();
                    if (record != null && record.positionStart != 0) {
                        mAdapter.notifyItemRangeInserted(record.positionStart, record.itemCount);
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }
                }//end if

                break;

        }//end switch
    }
}//end class
