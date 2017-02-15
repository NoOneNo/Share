package com.hengye.share.module.topic;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.module.util.encapsulation.base.TaskState;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.SPUtil;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.handler.TopicAdapterIdPager;
import com.hengye.share.util.handler.TopicIdHandler;
import com.hengye.share.module.util.encapsulation.base.DataType;
import com.hengye.share.model.Topic;
import com.hengye.share.util.UserUtil;

import java.util.ArrayList;

public class TopicFragment extends StatusFragment implements TopicContract.View{

    public static Bundle getBundle(TopicPresenter.TopicGroup topicGroup, String uid, String name) {
        return getBundle(topicGroup, false, uid, name);
    }

    public static Bundle getBundle(TopicPresenter.TopicGroup topicGroup, boolean isRestore, String uid, String name) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("topicGroup", topicGroup);
        bundle.putBoolean("isRestore", isRestore);
        bundle.putString("uid", uid);
        bundle.putString("name", name);
        return bundle;
    }

    public static TopicFragment newInstance(TopicPresenter.TopicGroup topicGroup, String uid, String name, boolean isRestore) {
        TopicFragment fragment = new TopicFragment();
        fragment.setArguments(getBundle(topicGroup, isRestore, uid, name));
        return fragment;
    }

    public static TopicFragment newInstance(TopicPresenter.TopicGroup topicGroup) {
        return newInstance(topicGroup, false);
    }

    public static TopicFragment newInstance(TopicPresenter.TopicGroup topicGroup, boolean isRestore) {
        return newInstance(topicGroup, null, null, isRestore);
    }

//    public static TopicFragment newInstance(TopicPresenter.TopicType topicType, String uid, String name) {
//        return newInstance(new TopicPresenter.TopicGroup(topicType), uid, name, false);
//    }

    TopicPresenter mPresenter;
    TopicPresenter.TopicGroup topicGroup;
    String uid, name;
    boolean isRestore;

    TopicAdapterIdPager mTopicPager;

    @Override
    public TopicAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    protected void handleBundleExtra(Bundle bundle) {
        topicGroup = (TopicPresenter.TopicGroup) bundle.getSerializable("topicGroup");
        isRestore = bundle.getBoolean("isRestore");
        uid = bundle.getString("uid");
        name = bundle.getString("name");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setPager(mTopicPager = new TopicAdapterIdPager(mAdapter));
        setDataHandler();
        mAdapter.setShowDeleteTopicOption(true);

        mPresenter = new TopicPresenter(this, topicGroup);
        mPresenter.setUid(uid);
        mPresenter.setName(name);
        mAdapter.setTopicPresenter(mPresenter);

        if (isRestore && !UserUtil.isUserEmpty()) {
            onRestore();
        } else {
            showLoading();
            markLazyLoadPreparedAndLazyLoadIfCan();
        }
    }

    protected void setDataHandler(){
        setDataHandler(new TopicIdHandler<>(mAdapter));
    }

    private void onRestore() {
        ArrayList<Topic> topic = mPresenter.findData();
        if (!CommonUtil.isEmpty(topic)) {
            int position = SPUtil.getInt("lastTopicPosition", 0);
            int offset = SPUtil.getInt("lastTopicOffset", 0);
            onLoadListData(true, topic);
            if (!mAdapter.isIndexOutOfBounds(position)) {
                getLayoutManager().scrollToPositionWithOffset(position, offset);
//                L.debug("onRestore Success, lastPosition : {}, offset : {}", position, offset);
            }
        } else {
            onRefresh();
        }
    }

    @Override
    protected void onLazyLoad() {
        if (!UserUtil.isUserEmpty()) {
            loadTopic();
        }else{
            showEmpty();
        }
    }

    @Override
    public void onTaskStart() {
        super.onTaskStart();
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

    /**
     * 先检测有没有缓存，没有再请求服务器
     */
    public void loadTopic() {
        mPresenter.loadWBTopic(mTopicPager.getFirstPage());
    }

    @Override
    public void handleDataType(int type) {
        super.handleDataType(type);
        if (DataType.hasNewData(type)) {
            mPresenter.saveData(mAdapter.getData());
        }
    }

    @Override
    public void deleteTopicResult(int taskState, Topic topic) {
        if (TaskState.isSuccess(taskState)) {
            mAdapter.removeItem(topic);
            ToastUtil.showToastSuccess(R.string.label_topic_destroy_success);
            mPresenter.clearCache();
        } else {
            ToastUtil.showToast(TaskState.toString(taskState));
        }
    }
}
