package com.hengye.share.ui.fragment.encapsulation.paging;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;

import com.hengye.share.R;
import com.hengye.swiperefresh.PullToRefreshLayout;
import com.hengye.swiperefresh.listener.SwipeListener;

import static com.hengye.share.handler.data.base.DataType.LOAD_NO_DATA;

/**
 * Created by yuhy on 16/7/27.
 */
public abstract class RecyclerRefreshFragment<T> extends RecyclerFragment<T> {

    @Override
    public int getContentResId() {
        return R.layout.fragment_recycler_refresh;
    }

    public
    @IdRes
    int getPullToRefreshId() {
        return R.id.pull_to_refresh;
    }

    public PullToRefreshLayout getPullToRefresh() {
        return mPullToRefresh;
    }

    public void onRefresh() {

    }

    public void onLoad() {

    }

    public void onTaskComplete() {
        mPullToRefresh.onTaskComplete();
    }

    public void setRefreshEnable(boolean enable) {
        mPullToRefresh.setRefreshEnable(enable);
    }

    public void setLoadEnable(boolean enable) {
        mPullToRefresh.setLoadEnable(enable);
    }

    public void setRefreshing(boolean refreshing) {
        mPullToRefresh.setRefreshing(refreshing);
    }

    public void setLoading(boolean loading) {
        mPullToRefresh.setLoading(loading);
    }

    @Override
    protected void initContent(@Nullable Bundle savedInstanceState) {
        super.initContent(savedInstanceState);

        mPullToRefresh = (PullToRefreshLayout) findViewById(getPullToRefreshId());
        mPullToRefresh.setOnRefreshListener(new SwipeListener.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RecyclerRefreshFragment.this.onRefresh();
            }
        });
        mPullToRefresh.setOnLoadListener(new SwipeListener.OnLoadListener() {
            @Override
            public void onLoad() {
                RecyclerRefreshFragment.this.onLoad();
            }
        });
    }

    PullToRefreshLayout mPullToRefresh;

    @Override
    public void updatePagingConfig(PagingConfig pagingConfig) {
        setLoadEnable(pagingConfig.isLoadEnable());
    }

    @Override
    public void handleDataType(int type) {
        getPagingConfig().setLoadEnable(type != LOAD_NO_DATA);
//        switch (type) {
//            case REFRESH_DATA_SIZE_EQUAL:
//                pullToRefreshLayout.setLoadEnable(true);
//                break;
//            case LOAD_NO_DATA:
//                pullToRefreshLayout.setLoadEnable(false);
//                break;
//            case LOAD_NO_MORE_DATA:
//                pullToRefreshLayout.setLoadEnable(false);
//                break;
//        }
    }
}
















