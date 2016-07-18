package com.hengye.share.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hengye.share.R;
import com.hengye.share.adapter.recyclerview.TopicAdapter;
import com.hengye.share.model.Topic;
import com.hengye.share.ui.base.BaseFragment;
import com.hengye.share.ui.mvpview.TopicMvpView;
import com.hengye.share.ui.presenter.TopicPresenter;
import com.hengye.share.ui.view.BackTopButton;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.DataUtil;
import com.hengye.share.util.UserUtil;
import com.hengye.swiperefresh.PullToRefreshLayout;
import com.hengye.swiperefresh.listener.SwipeListener;

import java.util.ArrayList;
import java.util.List;

public class TopicFragment extends BaseFragment implements TopicMvpView {

    public static TopicFragment newInstance(TopicPresenter.TopicGroup topicGroup, String uid, String name) {
        TopicFragment fragment = new TopicFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("topicGroup", topicGroup);
        bundle.putSerializable("uid", uid);
        bundle.putSerializable("name", name);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static TopicFragment newInstance(TopicPresenter.TopicGroup topicGroup) {
        return newInstance(topicGroup, null, null);
    }

    public static TopicFragment newInstance(TopicPresenter.TopicType topicType, String uid, String name) {
        return newInstance(new TopicPresenter.TopicGroup(topicType), uid, name);
    }

    private PullToRefreshLayout mPullToRefreshLayout;

    private TopicAdapter mAdapter;
    private TopicPresenter mPresenter;
    private TopicPresenter.TopicGroup topicGroup;
    private String uid, name;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_topic;
    }

    @Override
    protected void handleBundleExtra() {
        topicGroup = (TopicPresenter.TopicGroup) getArguments().getSerializable("topicGroup");
        uid = getArguments().getString("uid");
        name = getArguments().getString("name");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        addPresenter(mPresenter = new TopicPresenter(this, topicGroup));
        mPresenter.setUid(uid);
        mPresenter.setName(name);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter = new TopicAdapter(getContext(), new ArrayList<Topic>(), recyclerView));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        BackTopButton backTopBtn = (BackTopButton) findViewById(R.id.iv_back_top);
        backTopBtn.setup(recyclerView);

        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh);
        mPullToRefreshLayout.setOnRefreshListener(new SwipeListener.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (UserUtil.isUserEmpty()) {
                    mPullToRefreshLayout.setRefreshing(false);
                    return;
                }

                if (!mAdapter.isEmpty()) {
                    String id = mAdapter.getData().get(0).getId();
//                    mPresenter.loadWBAllTopicIds(id);
                    mPresenter.loadWBTopic(id, true);
                } else {
                    mPresenter.loadWBTopic("0", true);
                }
            }
        });

        mPullToRefreshLayout.setOnLoadListener(new SwipeListener.OnLoadListener() {
            @Override
            public void onLoad() {
                if (!mAdapter.isEmpty()) {
                    String id = CommonUtil.getLastItem(mAdapter.getData()).getId();
                    mPresenter.loadWBTopic(id, false);
                } else {
                    mPullToRefreshLayout.setLoading(false);
                    mPullToRefreshLayout.setLoadEnable(false);
                }
            }
        });


        getLoadDataCallBack().initView();

        mPresenter.loadCacheData();

//        if(SettingHelper.isPreRead()){
//            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                    super.onScrolled(recyclerView, dx, dy);
//                    if(layoutManager.findLastVisibleItemPosition() > mAdapter.getItemCount() - 8){
//                        mPullToRefreshLayout.setLoading(true);
//                    }
//                }
//            });
//        }
    }


    public void refresh() {
        mPresenter.loadCacheData();
    }

    @Override
    public void handleCache(List<Topic> data) {
        if (CommonUtil.isEmpty(data)) {
            getLoadDataCallBack().refresh(true);
//            mPullToRefreshLayout.setRefreshing(true);
        } else {
            mAdapter.refresh(data);
        }

    }

    @Override
    public void stopLoading(boolean isRefresh) {
        if (isRefresh) {
            getLoadDataCallBack().refresh(false);
//            mPullToRefreshLayout.setRefreshing(false);
        } else {
            mPullToRefreshLayout.setLoading(false);
        }
    }

    @Override
    public void handleNoMoreTopics() {
        Snackbar.make(mPullToRefreshLayout, "没有新的微博", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void handleTopicData(List<Topic> data, boolean isRefresh) {
        int type = DataUtil.handlePagingData(mAdapter.getData(), data, isRefresh);
        DataUtil.handleTopicAdapter(type, mAdapter, data);
        DataUtil.handlePullToRefresh(type, mPullToRefreshLayout);
        DataUtil.handleSnackBar(type, mPullToRefreshLayout, data == null ? 0 : data.size());
        if (type == DataUtil.REFRESH_DATA_SIZE_LESS
                || type == DataUtil.REFRESH_DATA_SIZE_EQUAL
                || type == DataUtil.LOAD_NO_MORE_DATA
                || type == DataUtil.LOAD_DATA_SIZE_EQUAL) {
            mPresenter.saveData(mAdapter.getData());
        }
    }

    LoadDataCallBack mLoadDataCallBack;

    public LoadDataCallBack getLoadDataCallBack() {
        if(mLoadDataCallBack == null){
            mLoadDataCallBack = new DefaultLoadDataCallBack();
        }
        return mLoadDataCallBack;
    }

    public void setLoadDataCallBack(LoadDataCallBack loadDataCallBack) {
        this.mLoadDataCallBack = loadDataCallBack;
    }

    public interface LoadDataCallBack {
        void initView();

        void refresh(boolean isRefresh);
    }

    public class DefaultLoadDataCallBack implements LoadDataCallBack {
        @Override
        public void initView() {

        }

        @Override
        public void refresh(boolean isRefresh) {
            mPullToRefreshLayout.setRefreshing(isRefresh);
        }
    }
}
