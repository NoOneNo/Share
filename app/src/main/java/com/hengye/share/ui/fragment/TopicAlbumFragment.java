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
import com.hengye.share.handler.data.TopicIdHandler;
import com.hengye.share.handler.data.TopicIdPager;
import com.hengye.share.handler.data.base.DataHandler;
import com.hengye.share.handler.data.base.Pager;
import com.hengye.share.model.Topic;
import com.hengye.share.ui.activity.GalleryActivity;
import com.hengye.share.ui.fragment.PersonalHomepageFragment.LoadDataCallBack;
import com.hengye.share.ui.fragment.encapsulation.paging.RecyclerRefreshFragment;
import com.hengye.share.ui.mvpview.TopicAlbumMvpView;
import com.hengye.share.ui.presenter.TopicAlbumPresenter;
import com.hengye.share.ui.view.listener.OnItemClickListener;
import com.hengye.share.util.CommonUtil;
import com.hengye.swiperefresh.listener.SwipeListener;

import java.util.ArrayList;

public class TopicAlbumFragment extends RecyclerRefreshFragment<String> implements TopicAlbumMvpView, SwipeListener.OnRefreshListener {

    public static TopicAlbumFragment newInstance(String uid, String name) {
        TopicAlbumFragment fragment = new TopicAlbumFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("uid", uid);
        bundle.putSerializable("name", name);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ArrayList<Topic> topics;
    public static ArrayList<String> urls;

    TopicAlbumAdapter mAdapter;
    TopicAlbumPresenter mPresenter;
    ArrayList<Topic> mTopics;
    String uid, name;

    TopicIdPager mTopicPager;
    TopicIdHandler<String> mHandler;

    @Override
    public int getContentResId() {
        return R.layout.fragment_topic_album;
    }

    @Override
    protected void handleBundleExtra(Bundle bundle) {
        uid = bundle.getString("uid");
        name = bundle.getString("name");
    }

    @Override
    public boolean onToolbarDoubleClick(Toolbar toolbar) {
        getRecyclerView().getLayoutManager().scrollToPosition(0);
        return true;
    }

    public void handleStaticData(boolean isReset){
        if(isReset){
            topics = null;
            urls = null;
        }else{
            topics = mTopics;
            urls = (ArrayList<String>) mAdapter.getData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        handleStaticData(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handleStaticData(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setAdapter(mAdapter = new TopicAlbumAdapter(getContext(), new ArrayList<String>()));
        mTopicPager = new TopicIdPager(mTopics);
        mHandler = new TopicIdHandler<>(mAdapter);

        addPresenter(mPresenter = new TopicAlbumPresenter(this));
        mPresenter.setUid(uid);
        mPresenter.setName(name);

        getPullToRefresh().setRefreshEnable(false);

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                handleStaticData(false);
                GalleryActivity.startWithIntent(getActivity(), null, position, null, null);
            }
        });
        mPresenter.loadCacheData();
//        onRefresh();
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(getContext(), 3, LinearLayoutManager.VERTICAL, false);
    }

    @Override
    public void onRefresh() {
        super.onLoad();
        mPresenter.loadTopicAlbum(mTopicPager.getFirstPage(), true);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        mPresenter.loadTopicAlbum(mTopicPager.getNextPage(), false);
    }

    @Override
    public Pager getPager() {
        return mTopicPager;
    }

    @Override
    public DataHandler<String> getDataHandler() {
        return mHandler;
    }

    @Override
    public void handleCache(ArrayList<Topic> topics, ArrayList<String> urls) {
        if(CommonUtil.isEmpty(urls)){
            onRefresh();
        }else{
            handleTopics(topics, true);
            mAdapter.refresh(urls);
            getPullToRefresh().setLoadEnable(true);
        }
    }

    @Override
    public void stopLoading(boolean isRefresh) {
        if (isRefresh) {
            getLoadDataCallBack().refresh(false);
        } else {
            getPullToRefresh().setLoading(false);
        }
    }

    @Override
    public void handleAlbumData(ArrayList<Topic> topics, ArrayList<String> urls, boolean isRefresh) {
        handleData(isRefresh, urls);
        handleTopics(topics, isRefresh);
    }

    public void handleTopics(ArrayList<Topic> topics, boolean isRefresh){
        if(isRefresh) {
            if(mTopics != null) {
                mTopics.clear();
            }
            mTopics = topics;
            mTopicPager.refreshIds(mTopics);
        }else{
            mTopics.addAll(topics);
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

    public class DefaultLoadDataCallBack implements LoadDataCallBack {
        @Override
        public void initView() {

        }

        @Override
        public void refresh(boolean isRefreshing) {
            getPullToRefresh().setRefreshing(isRefreshing);
        }
    }
}
