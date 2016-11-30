package com.hengye.share.module.topic;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hengye.share.util.handler.TopicAdapterIdPager;
import com.hengye.share.util.handler.TopicIdHandler;
import com.hengye.share.module.util.encapsulation.base.DataHandler;
import com.hengye.share.module.util.encapsulation.base.DataType;
import com.hengye.share.module.util.encapsulation.base.Pager;
import com.hengye.share.model.Topic;
import com.hengye.share.util.UserUtil;

import java.util.ArrayList;

public class TopicFragment extends StatusFragment<Topic> implements TopicMvpView {

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

    @Override
    protected void handleBundleExtra(Bundle bundle) {
        topicGroup = (TopicPresenter.TopicGroup) bundle.getSerializable("topicGroup");
        uid = bundle.getString("uid");
        name = bundle.getString("name");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        setAdapter(mAdapter = new TopicAdapter(getContext(), new ArrayList<Topic>(), getRecyclerView()));
        mTopicPager = new TopicAdapterIdPager(mAdapter);
        mHandler = new TopicIdHandler<>(mAdapter);

        addPresenter(mPresenter = new TopicPresenter(this, topicGroup));
        mPresenter.setUid(uid);
        mPresenter.setName(name);

        showLoading();
        markLazyLoadPreparedAndLazyLoadIfCan();
    }

    @Override
    protected void onLazyLoad() {
        if(!UserUtil.isUserEmpty()) {
            loadTopic();
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
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

    /**
     * 先检测有没有缓存，没有再请求服务器
     */
    public void loadTopic() {
        mPresenter.loadWBTopic(mTopicPager.getFirstPage());
    }

    @Override
    public void handleDataType(int type) {
        super.handleDataType(type);
        if(DataType.hasNewData(type)){
            mPresenter.saveData(mAdapter.getData());
        }
    }

}
