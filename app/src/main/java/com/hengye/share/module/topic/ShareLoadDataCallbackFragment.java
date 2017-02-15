package com.hengye.share.module.topic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.module.base.ShareRecyclerFragment;
import com.hengye.share.ui.widget.fab.FabAnimator;
import com.hengye.share.util.thirdparty.WBUtil;

import java.util.List;

/**
 * Created by yuhy on 2016/10/25.
 */

public abstract class ShareLoadDataCallbackFragment<T> extends ShareRecyclerFragment<T> {

    private LinearLayoutManager mLinearLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLinearLayoutManager = (LinearLayoutManager) getRecyclerView().getLayoutManager();

        View fab = getActivity().findViewById(R.id.fab);
        if (fab != null) {
            FabAnimator.create(fab).attachToRecyclerView(getRecyclerView());
        }

        getLoadDataCallBack().initView();
    }

    public LinearLayoutManager getLayoutManager() {
        return mLinearLayoutManager;
    }

    @Override
    public void onTaskComplete(boolean isRefresh, int taskState) {
        super.onTaskComplete(isRefresh, taskState);
        if (isRefresh) {
            getLoadDataCallBack().refresh(false);
        }
    }

    @Override
    public boolean canLoadMore(List<T> data, int type) {
        int pageSize;
        if (getPager() != null) {
            pageSize = getPager().getPageSize();
        } else {
            pageSize = WBUtil.getWBTopicRequestCount();
        }

        if (data != null && data.size() < pageSize / 2) {
            return false;
        } else {
            return super.canLoadMore(data, type);
        }
    }

    private LoadDataCallback mLoadDataCallback;

    public LoadDataCallback getLoadDataCallBack() {
        if (mLoadDataCallback == null) {
            mLoadDataCallback = new DefaultLoadDataCallback();
        }
        return mLoadDataCallback;
    }

    public void setLoadDataCallBack(LoadDataCallback loadDataCallback) {
        this.mLoadDataCallback = loadDataCallback;
    }

    public class DefaultLoadDataCallback implements LoadDataCallback {
        @Override
        public void initView() {

        }

        @Override
        public void refresh(boolean isRefreshing) {
            if (isRefreshing) {
                onRefresh();
            } else {
                setRefreshing(false);
            }
        }
    }

    public interface LoadDataCallback {

        void initView();

        void refresh(boolean isRefreshing);
    }
}
