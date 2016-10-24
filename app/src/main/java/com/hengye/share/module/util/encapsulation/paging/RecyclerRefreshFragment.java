package com.hengye.share.module.util.encapsulation.paging;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.hengye.share.R;
import com.hengye.share.ui.widget.pulltorefresh.PullToRefreshLayout;
import com.hengye.share.util.L;
import com.hengye.swiperefresh.listener.SwipeListener;

import java.util.List;

import static com.hengye.share.module.util.encapsulation.paging.TaskState.*;

/**
 * Created by yuhy on 16/7/27.
 */
public abstract class RecyclerRefreshFragment<T> extends RecyclerFragment<T>{

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

    @Override
    public void onRetry() {
        showLoading();
        onRefresh();
    }

    public void onRefresh() {

    }

    public void onLoad() {

    }

    public void onTaskComplete(boolean isSuccess) {
        if (mPullToRefresh != null) {
            mPullToRefresh.onTaskComplete(isSuccess);
        }
    }

    public void setRefreshEnable(boolean enable) {
        if (mPullToRefresh != null) {
            mPullToRefresh.setRefreshEnable(enable);
        }
    }

    public void setLoadEnable(boolean enable) {
        if (mPullToRefresh != null) {
            mPullToRefresh.setLoadEnable(enable);
        }
    }


    public void setRefreshing(final boolean refreshing) {
        if (mPullToRefresh != null) {
            mPullToRefresh.setRefreshing(refreshing);
        }
    }

    public void startLoading() {
        if (mPullToRefresh != null) {
            mPullToRefresh.startLoading();
        }
    }

    public void stopLoading(boolean isSuccess) {
        if (mPullToRefresh != null) {
            mPullToRefresh.stopLoading(isSuccess);
        }
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
    public void onTaskComplete(boolean isRefresh, int taskState) {
        onTaskComplete(isSuccess(taskState));
        super.onTaskComplete(isRefresh, taskState);
    }

    @Override
    public boolean canLoadMore(List<T> data, int type) {
        if(isLEChildCount()){
            return false;
        }else {
            return super.canLoadMore(data, type);
        }
    }

    /**
     * adapter的内容是否都已经显示完；
     * @return
     */
    public boolean isLEChildCount(){
        if(getRecyclerView().getLayoutManager() != null){
            RecyclerView.LayoutManager layoutManager = getRecyclerView().getLayoutManager();

            L.debug("childCount : {}, itemCount : {}", layoutManager.getChildCount(), layoutManager.getItemCount());
            if(layoutManager.getChildCount() >= layoutManager.getItemCount()){
                return true;
            }
        }
        return false;
    }
}
















