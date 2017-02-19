package com.hengye.share.module.status;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.hengye.share.module.util.encapsulation.base.DefaultDataHandler;
import com.hengye.share.util.handler.StatusNumberPager;
import com.hengye.share.module.util.encapsulation.base.DataType;
import com.hengye.share.model.Status;

import java.util.List;

public class StatusPageFragment extends StatusActionFragment implements StatusPageContract.View {

    public static Bundle getStartBundle(StatusPagePresenter.StatusGroup statusGroup, String keyword) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("topicGroup", statusGroup);
        bundle.putString("keyword", keyword);
        return bundle;
    }

    public static StatusPageFragment newInstance(StatusPagePresenter.StatusGroup statusGroup, String keyword) {
        StatusPageFragment fragment = new StatusPageFragment();
        fragment.setArguments(getStartBundle(statusGroup, keyword));
        return fragment;
    }

    public static StatusPageFragment newInstance(StatusPagePresenter.StatusType statusType, String keyword) {
        return newInstance(new StatusPagePresenter.StatusGroup(statusType), keyword);
    }

    private StatusPagePresenter mPresenter;
    private StatusPagePresenter.StatusGroup statusGroup;

    private StatusNumberPager mPager;
    private String mKeyword;

    @Override
    protected void handleBundleExtra(Bundle bundle) {
        statusGroup = (StatusPagePresenter.StatusGroup) bundle.getSerializable("topicGroup");
        mKeyword = bundle.getString("keyword");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setPager(mPager = new StatusNumberPager());
        setDataHandler(new DefaultDataHandler<>(mAdapter));
        mPresenter = new StatusPagePresenter(this, statusGroup, mPager);
        mPresenter.setKeyword(mKeyword);

        showLoading();
        markLazyLoadPreparedAndLazyLoadIfCan();
    }

    @Override
    protected void onLazyLoad() {
        mPresenter.loadWBStatus();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mPresenter.loadRemoteWBStatus(true);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        mPresenter.loadRemoteWBStatus(false);
    }

    public void refresh(String keyword) {
        if (keyword == null || TextUtils.isEmpty(keyword.trim())) {
            return;
        }
        mPresenter.setKeyword(keyword);
        showLoading();
        onRefresh();
    }

    @Override
    public void onLoadListData(boolean isRefresh, List<Status> data) {
        super.onLoadListData(isRefresh, data);
    }

    @Override
    public void handleDataType(int type) {
        super.handleDataType(type);
        if(DataType.hasNewData(type)){
            mPresenter.saveData(mAdapter.getData());
        }
    }
}
