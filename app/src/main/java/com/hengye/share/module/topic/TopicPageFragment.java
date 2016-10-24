package com.hengye.share.module.topic;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.hengye.share.handler.data.DefaultDataHandler;
import com.hengye.share.handler.data.NumberPager;
import com.hengye.share.handler.data.base.DataHandler;
import com.hengye.share.handler.data.base.DataType;
import com.hengye.share.handler.data.base.Pager;
import com.hengye.share.model.Topic;
import com.hengye.share.module.util.encapsulation.paging.RecyclerRefreshFragment;
import com.hengye.share.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

public class TopicPageFragment extends RecyclerRefreshFragment<Topic> implements TopicPageMvpView {

    public static TopicPageFragment newInstance(TopicPagePresenter.TopicGroup topicGroup, String keyword) {
        TopicPageFragment fragment = new TopicPageFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("topicGroup", topicGroup);
        bundle.putString("keyword", keyword);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static TopicPageFragment newInstance(TopicPagePresenter.TopicType topicType, String keyword) {
        return newInstance(new TopicPagePresenter.TopicGroup(topicType), keyword);
    }

    private TopicAdapter mAdapter;
    private TopicPagePresenter mPresenter;
    private TopicPagePresenter.TopicGroup topicGroup;

    private NumberPager mPager;
    private DefaultDataHandler<Topic> mHandler;
    private String mKeyword;

    @Override
    protected void handleBundleExtra(Bundle bundle) {
        topicGroup = (TopicPagePresenter.TopicGroup) bundle.getSerializable("topicGroup");
        mKeyword = bundle.getString("keyword");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAdapter(mAdapter = new TopicAdapter(getContext(), new ArrayList<Topic>(), getRecyclerView()));
        mPager = new NumberPager();
        mHandler = new DefaultDataHandler<>(mAdapter);
        addPresenter(mPresenter = new TopicPagePresenter(this, topicGroup, mPager));
        mPresenter.setKeyword(mKeyword);
        setRefreshing(true);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (UserUtil.isUserEmpty()) {
            setRefreshing(false);
            return;
        }
        mPresenter.loadWBTopic(true);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!mAdapter.isEmpty()) {
            mPresenter.loadWBTopic(false);
        }
    }

    @Override
    public Pager getPager() {
        return mPager;
    }

    @Override
    public DataHandler<Topic> getDataHandler() {
        return mHandler;
    }

    public void refresh(String keyword) {
        if (keyword == null || TextUtils.isEmpty(keyword.trim())) {
            return;
        }
        mPresenter.setKeyword(keyword);
        setRefreshing(true);
    }

    @Override
    public void onTaskComplete(boolean isRefresh, boolean isSuccess) {
        onTaskComplete(isSuccess);
    }

    @Override
    public void handleTopicData(List<Topic> data, boolean isRefresh) {
        int type = handleData(isRefresh, data);
    }

    @Override
    public void handleDataType(int type) {
        super.handleDataType(type);
        if(type == DataType.LOAD_NO_DATA || type == DataType.REFRESH_DATA_SIZE_LESS){
            setLoadEnable(false);
        }else{
            setLoadEnable(true);
        }
    }
}
