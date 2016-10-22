package com.hengye.share.module.topic;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.handler.data.TopicIdHandler;
import com.hengye.share.handler.data.TopicAdapterIdPager;
import com.hengye.share.handler.data.base.DataHandler;
import com.hengye.share.handler.data.base.DataType;
import com.hengye.share.handler.data.base.Pager;
import com.hengye.share.model.Topic;
import com.hengye.share.module.profile.PersonalHomepageFragment.LoadDataCallBack;
import com.hengye.share.module.util.encapsulation.paging.RecyclerRefreshFragment;
import com.hengye.share.ui.widget.fab.FabAnimator;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.thirdparty.WBUtil;

import java.util.ArrayList;
import java.util.List;

public class TopicFragment extends RecyclerRefreshFragment<Topic> implements TopicMvpView {

    public static Bundle getBundle(TopicPresenter.TopicGroup topicGroup, String uid, String name){
        Bundle bundle = new Bundle();
        bundle.putSerializable("topicGroup", topicGroup);
        bundle.putSerializable("uid", uid);
        bundle.putSerializable("name", name);
        return bundle;
    }

    public static TopicFragment newInstance(TopicPresenter.TopicGroup topicGroup, String uid, String name) {
        TopicFragment fragment = new TopicFragment();
        fragment.setArguments(getBundle(topicGroup, uid, name));
        return fragment;
    }

    public static TopicFragment newInstance(TopicPresenter.TopicGroup topicGroup) {
        return newInstance(topicGroup, null, null);
    }

    public static TopicFragment newInstance(TopicPresenter.TopicType topicType, String uid, String name) {
        return newInstance(new TopicPresenter.TopicGroup(topicType), uid, name);
    }

    TopicAdapter mAdapter;
    TopicPresenter mPresenter;
    TopicPresenter.TopicGroup topicGroup;
    String uid, name;
    LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void handleBundleExtra(Bundle bundle) {
        topicGroup = (TopicPresenter.TopicGroup) bundle.getSerializable("topicGroup");
        uid = bundle.getString("uid");
        name = bundle.getString("name");
    }

    @Override
    public boolean onToolbarDoubleClick(Toolbar toolbar) {
        getRecyclerView().getLayoutManager().scrollToPosition(0);
        return true;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        mLinearLayoutManager = (LinearLayoutManager)getRecyclerView().getLayoutManager();
        setAdapter(mAdapter = new TopicAdapter(getContext(), new ArrayList<Topic>(), getRecyclerView()));
        mTopicPager = new TopicAdapterIdPager(mAdapter);
        mHandler = new TopicIdHandler<>(mAdapter);

        addPresenter(mPresenter = new TopicPresenter(this, topicGroup));
        mPresenter.setUid(uid);
        mPresenter.setName(name);

        getLoadDataCallBack().initView();

        if(!UserUtil.isUserEmpty()) {
            refresh();
        }

        View fab = getActivity().findViewById(R.id.fab);
        if(fab != null) {
            FabAnimator.create(fab).attachToRecyclerView(getRecyclerView());
        }
    }

    public void scrollToTop(){
        if(getRecyclerView() != null) {
            getRecyclerView().scrollToPosition(0);
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
//        if (UserUtil.isUserEmpty()) {
//            setRefreshing(false);
//            return;
//        }
        mPresenter.loadWBTopic(mTopicPager.getFirstPage(), true);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        mPresenter.loadWBTopic(mTopicPager.getNextPage(), false);
    }

    TopicAdapterIdPager mTopicPager;
    TopicIdHandler<Topic> mHandler;

    @Override
    public DataHandler<Topic> getDataHandler() {
        return mHandler;
    }

    @Override
    public Pager getPager() {
        return mTopicPager;
    }

    public void refresh() {
        mPresenter.loadWBTopic(mTopicPager.getFirstPage());
    }

    public void handleNoMoreTopics() {
        Snackbar.make(getParent(), "没有新的微博", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onTaskComplete(boolean isRefresh, int taskState) {
        super.onTaskComplete(isRefresh, taskState);
        if(isRefresh){
            getLoadDataCallBack().refresh(false);
        }
    }

    @Override
    public void onLoadListData(boolean isRefresh, List<Topic> data) {
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
    public void handleDataType(int type) {
        super.handleDataType(type);
        if(DataType.hasNewData(type)){
            mPresenter.saveData(mAdapter.getData());
        }
    }

    @Override
    public boolean canLoadMore(List<Topic> data, int type) {
        if(data != null && data.size() < WBUtil.getWBTopicRequestCount() / 2){
            return false;
        }else {
            return super.canLoadMore(data, type);
        }
    }

    LoadDataCallBack mLoadDataCallBack;

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
}
