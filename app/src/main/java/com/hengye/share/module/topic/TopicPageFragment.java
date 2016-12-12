package com.hengye.share.module.topic;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.hengye.share.module.util.encapsulation.base.DefaultDataHandler;
import com.hengye.share.util.handler.TopicNumberPager;
import com.hengye.share.module.util.encapsulation.base.DataHandler;
import com.hengye.share.module.util.encapsulation.base.DataType;
import com.hengye.share.module.util.encapsulation.base.Pager;
import com.hengye.share.model.Topic;

import java.util.ArrayList;
import java.util.List;

public class TopicPageFragment extends StatusFragment<Topic> implements TopicPageMvpView {

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

    private TopicNumberPager mPager;
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
        setPager(mPager = new TopicNumberPager());
        setDataHandler(new DefaultDataHandler<>(mAdapter));
        addPresenter(mPresenter = new TopicPagePresenter(this, topicGroup, mPager));
        mPresenter.setKeyword(mKeyword);
        showLoading();
        mPresenter.loadWBTopic();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mPresenter.loadRemoteWBTopic(true);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        mPresenter.loadRemoteWBTopic(false);
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
    public void onLoadListData(boolean isRefresh, List<Topic> data) {
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
