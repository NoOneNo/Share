package com.hengye.share.module.topic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.module.util.encapsulation.fragment.RecyclerRefreshFragment;
import com.hengye.share.module.util.encapsulation.view.listener.OnDoubleTapListener;
import com.hengye.share.ui.widget.fab.FabAnimator;
import com.hengye.share.util.thirdparty.WBUtil;

import java.util.List;

/**
 * Created by yuhy on 2016/10/25.
 */

public abstract class StatusFragment<T> extends RecyclerRefreshFragment<T> {

    @Override
    public int getContentResId() {
        return R.layout.fragment_recycler_refresh_vertical;
    }

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
        if(fab != null) {
            FabAnimator.create(fab).attachToRecyclerView(getRecyclerView());
        }

        getLoadDataCallBack().initView();

        if(getActivity() instanceof BaseActivity){
            BaseActivity baseActivity = (BaseActivity) getActivity();
            if(baseActivity.getToolbar() != null){
                baseActivity.getToolbar().addOnDoubleTapListener(new OnDoubleTapListener() {
                    @Override
                    public void onDoubleTap(View view) {
                        onToolbarDoubleClick((Toolbar)view);
                    }
                });
            }
        }
    }

    public LinearLayoutManager getLayoutManager(){
        return mLinearLayoutManager;
    }

    @Override
    public void onToolbarDoubleClick(Toolbar toolbar) {
        onScrollToTop(false);
    }

    @Override
    public void onTaskComplete(boolean isRefresh, int taskState) {
        super.onTaskComplete(isRefresh, taskState);
        if(isRefresh){
            getLoadDataCallBack().refresh(false);
        }
    }

    @Override
    public void onLoadListData(boolean isRefresh, List<T> data) {
//        if (isRefresh
//                && topicGroup.isReadingType()
//                && !CommonUtil.isEmpty(data)
//                && !SettingHelper.isOrderReading()
//                && mLinearLayoutManager.findFirstVisibleItemPosition() == mAdapter.getHeaderPosition()) {
//            getRecyclerView().scrollToPosition(data.size() - 1);
//        }
        super.onLoadListData(isRefresh, data);
    }

    @Override
    public boolean canLoadMore(List<T> data, int type) {
        if(data != null && data.size() < WBUtil.getWBTopicRequestCount() / 2){
            return false;
        }else {
            return super.canLoadMore(data, type);
        }
    }

    private LoadDataCallBack mLoadDataCallBack;

    public LoadDataCallBack getLoadDataCallBack() {
        if (mLoadDataCallBack == null) {
            mLoadDataCallBack = new DefaultLoadDataCallBack();
        }
        return mLoadDataCallBack;
    }

    public void setLoadDataCallBack(LoadDataCallBack loadDataCallBack) {
        this.mLoadDataCallBack = loadDataCallBack;
    }

    public class DefaultLoadDataCallBack implements LoadDataCallBack {
        @Override
        public void initView() {

        }

        @Override
        public void refresh(boolean isRefreshing) {
            if(isRefreshing){
                onRefresh();
            }else{
                setRefreshing(false);
            }
        }
    }

    public interface LoadDataCallBack {

        void initView();

        void refresh(boolean isRefreshing);
    }
}
