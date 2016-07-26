package com.hengye.share.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.adapter.recyclerview.TopicAlbumAdapter;
import com.hengye.share.ui.activity.TopicGalleryActivity;
import com.hengye.share.ui.base.BaseFragment;
import com.hengye.share.ui.fragment.PersonalHomepageFragment.LoadDataCallBack;
import com.hengye.share.ui.mvpview.TopicAlbumMvpView;
import com.hengye.share.ui.presenter.TopicAlbumPresenter;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.ViewUtil;
import com.hengye.swiperefresh.PullToRefreshLayout;
import com.hengye.swiperefresh.listener.SwipeListener;

import java.util.ArrayList;
import java.util.List;

public class TopicAlbumFragment extends BaseFragment implements TopicAlbumMvpView, SwipeListener.OnRefreshListener {

    public static TopicAlbumFragment newInstance(String uid, String name) {
        TopicAlbumFragment fragment = new TopicAlbumFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("uid", uid);
        bundle.putSerializable("name", name);
        fragment.setArguments(bundle);
        return fragment;
    }

    PullToRefreshLayout mPullToRefresh;
    RecyclerView mRecyclerView;
    TopicAlbumAdapter mAdapter;

    TopicAlbumPresenter mPresenter;

    String uid, name;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_topic_album;
    }

    @Override
    protected void handleBundleExtra() {
        uid = getArguments().getString("uid");
        name = getArguments().getString("name");
    }

    @Override
    public boolean onToolbarDoubleClick(Toolbar toolbar) {
        mRecyclerView.getLayoutManager().scrollToPosition(0);
        return true;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addPresenter(mPresenter = new TopicAlbumPresenter(this));
        mPresenter.setUid(uid);
        mPresenter.setName(name);

        mPullToRefresh = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh);
        mPullToRefresh.setLoadEnable(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        GridLayoutManager staggeredGridLayoutManager = new GridLayoutManager(getContext(), 3, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setAdapter(mAdapter = new TopicAlbumAdapter(getContext(), new ArrayList<String>()));

        mPullToRefresh.setRefreshEnable(false);
        mPullToRefresh.setOnLoadListener(new SwipeListener.OnLoadListener() {
            @Override
            public void onLoad() {
                mPresenter.loadTopicAlbum(false);
            }
        });

        mAdapter.setOnItemClickListener(new ViewUtil.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TopicGalleryActivity.startWithIntent(getActivity(), (ArrayList<String>)mAdapter.getData(), position);
            }
        });

        mPresenter.loadCacheData();
//        onRefresh();
    }

    @Override
    public void handleCache(List<String> data) {
        if(CommonUtil.isEmpty(data)){
            onRefresh();
        }else{
            mAdapter.refresh(data);
        }
    }

    @Override
    public void stopLoading(boolean isRefresh) {
        if (isRefresh) {
            getLoadDataCallBack().refresh(false);
        } else {
            mPullToRefresh.setLoading(false);
        }
    }

    @Override
    public void handleAlbumData(List<String> urls, boolean isRefresh) {
        if (isRefresh) {
            mAdapter.refresh(urls);
            mPullToRefresh.setLoadEnable(true);
        } else {
            mAdapter.addAll(urls);
            if (CommonUtil.isEmpty(urls)) {
                mPullToRefresh.setLoadEnable(false);
            }
        }
        mPresenter.saveData(mAdapter.getData());
    }

    @Override
    public void onRefresh() {
        mPresenter.loadTopicAlbum(true);
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

    public PullToRefreshLayout getPullToRefresh(){
        return mPullToRefresh;
    }

    public class DefaultLoadDataCallBack implements LoadDataCallBack {
        @Override
        public void initView() {

        }

        @Override
        public void refresh(boolean isRefreshing) {
            mPullToRefresh.setRefreshing(isRefreshing);
        }
    }
}
