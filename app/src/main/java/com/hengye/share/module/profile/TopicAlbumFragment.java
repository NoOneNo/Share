package com.hengye.share.module.profile;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.model.Topic;
import com.hengye.share.module.topic.StatusFragment;
import com.hengye.share.module.base.ShareRecyclerFragment;
import com.hengye.share.module.util.encapsulation.view.listener.OnItemClickListener;
import com.hengye.share.module.util.image.GalleryActivity;
import com.hengye.share.util.L;
import com.hengye.share.util.handler.TopicIdPager;
import com.hengye.share.util.handler.TopicRefreshIdHandler;
import com.hengye.share.util.thirdparty.WBUtil;
import com.hengye.swiperefresh.listener.SwipeListener;

import java.util.ArrayList;

public class TopicAlbumFragment extends ShareRecyclerFragment<String> implements TopicAlbumContract.View, SwipeListener.OnRefreshListener {

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

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_content;
    }

    @Override
    public int getLoadingResId(){
        return R.layout.state_loading_top;
    }

    @Override
    public int getEmptyResId(){
        return R.layout.state_empty_top;
    }

    @Override
    public int getNoNetworkResId(){
        return R.layout.state_no_network_top;
    }

    @Override
    public int getServiceErrorResId() {
        return R.layout.state_service_error_top;
    }

    @Override
    public int getContentResId() {
        return R.layout.fragment_topic_album;
    }

    @Override
    protected void handleBundleExtra(Bundle bundle) {
        uid = bundle.getString("uid");
        name = bundle.getString("name");
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
        L.debug("album onResume invoke()");
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
        setPager(mTopicPager = new TopicIdPager(mTopics));
        setDataHandler(new TopicRefreshIdHandler<>(mAdapter));

        mPresenter = new TopicAlbumPresenter(this);
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

        showLoading();
        markLazyLoadPreparedAndLazyLoadIfCan();
    }

    protected void onLazyLoad() {
        mPresenter.loadCacheData();
    }

    @Override
    protected RecyclerView.LayoutManager generateLayoutManager() {
        return new GridLayoutManager(getContext(), 3, LinearLayoutManager.VERTICAL, false);
    }

    @Override
    public void onRefresh() {
        mPresenter.loadTopicAlbum(mTopicPager.getFirstPage(), true);
    }

    @Override
    public void onLoad() {
        mPresenter.loadTopicAlbum(mTopicPager.getNextPage(), false);
    }

    @Override
    public void onTaskComplete(boolean isRefresh, int taskState) {
        super.onTaskComplete(isRefresh, taskState);
        if(isRefresh){
            getLoadDataCallBack().refresh(false);
        }
    }

    @Override
    public void handleAlbumData(ArrayList<Topic> topics, ArrayList<String> urls, boolean isRefresh) {

        onLoadListData(isRefresh, urls);

        handleTopics(topics, isRefresh);

        if(topics != null && topics.size() < WBUtil.getWBTopicRequestCount()){
            setLoadEnable(false);
        }
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

    StatusFragment.LoadDataCallBack mLoadDataCallBack;

    public StatusFragment.LoadDataCallBack getLoadDataCallBack() {
        if(mLoadDataCallBack == null){
            mLoadDataCallBack = new DefaultLoadDataCallBack();
        }
        return mLoadDataCallBack;
    }

    public void setLoadDataCallBack(StatusFragment.LoadDataCallBack loadDataCallBack) {
        this.mLoadDataCallBack = loadDataCallBack;
    }

    public class DefaultLoadDataCallBack implements StatusFragment.LoadDataCallBack {
        @Override
        public void initView() {

        }

        @Override
        public void refresh(boolean isRefreshing) {
            getPullToRefresh().setRefreshing(isRefreshing);
        }
    }
}
