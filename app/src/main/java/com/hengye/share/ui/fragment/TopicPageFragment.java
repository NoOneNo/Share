package com.hengye.share.ui.fragment;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.hengye.share.R;
import com.hengye.share.adapter.recyclerview.TopicAdapter;
import com.hengye.share.model.Topic;
import com.hengye.share.ui.base.BaseFragment;
import com.hengye.share.ui.mvpview.TopicMvpView;
import com.hengye.share.ui.mvpview.TopicPageMvpView;
import com.hengye.share.ui.presenter.TopicPagePresenter;
import com.hengye.share.ui.presenter.TopicPresenter;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.DataUtil;
import com.hengye.share.util.UserUtil;
import com.hengye.swiperefresh.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class TopicPageFragment extends BaseFragment implements TopicPageMvpView {

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

    private PullToRefreshLayout mPullToRefreshLayout;
    private TopicAdapter mAdapter;
    private TopicPagePresenter mPresenter;
    private TopicPagePresenter.TopicGroup topicGroup;
    private String mKeyword;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_topic;
    }

    @Override
    protected void handleBundleExtra() {
        topicGroup = (TopicPagePresenter.TopicGroup)getArguments().getSerializable("topicGroup");
        mKeyword = getArguments().getString("keyword");
    }

    @Override
    protected void onCreateView() {

        setupPresenter(mPresenter = new TopicPagePresenter(this, topicGroup));
        mPresenter.setKeyword(mKeyword);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter = new TopicAdapter(getContext(), new ArrayList<Topic>(), recyclerView));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh);
        mPullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (UserUtil.isUserEmpty()) {
                    mPullToRefreshLayout.setRefreshing(false);
                    return;
                }

                mPresenter.loadWBTopic(true);
            }
        });

        mPullToRefreshLayout.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                if (!mAdapter.isEmpty()) {
                    mPresenter.loadWBTopic(false);
                } else {
                    mPullToRefreshLayout.setLoading(false);
                    mPullToRefreshLayout.setLoadEnable(false);
                }
            }
        });

        mPullToRefreshLayout.setRefreshing(true);
    }

    public void refresh(String keyword){
        if(keyword == null || TextUtils.isEmpty(keyword.trim())){
            return;
        }
        mPresenter.setKeyword(keyword);
        mPullToRefreshLayout.setRefreshing(true);
    }


    @Override
    public void stopLoading(boolean isRefresh) {
        if (isRefresh) {
            mPullToRefreshLayout.setRefreshing(false);
        } else {
            mPullToRefreshLayout.setLoading(false);
        }
    }

    @Override
    public void handleTopicData(List<Topic> data, boolean isRefresh) {
        int type = DataUtil.handlePagingData(mAdapter.getData(), data, isRefresh);
        DataUtil.handleTopicAdapter(type, mAdapter, data);
        DataUtil.handlePullToRefresh(type, mPullToRefreshLayout);
        DataUtil.handleSnackBar(type, mPullToRefreshLayout, data == null ? 0 : data.size());
    }

}
