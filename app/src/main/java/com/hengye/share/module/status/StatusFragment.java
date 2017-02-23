package com.hengye.share.module.status;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.module.util.encapsulation.base.TaskState;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.SPUtil;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.handler.StatusAdapterIdPager;
import com.hengye.share.util.handler.StatusIdHandler;
import com.hengye.share.module.util.encapsulation.base.DataType;
import com.hengye.share.model.Status;
import com.hengye.share.util.UserUtil;

import java.util.ArrayList;

public class StatusFragment extends StatusActionFragment implements StatusContract.View{

    public static Bundle getBundle(StatusPresenter.StatusGroup statusGroup, String uid, String name) {
        return getBundle(statusGroup, false, uid, name);
    }

    public static Bundle getBundle(StatusPresenter.StatusGroup statusGroup, boolean isRestore, String uid, String name) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("statusGroup", statusGroup);
        bundle.putBoolean("isRestore", isRestore);
        bundle.putString("uid", uid);
        bundle.putString("name", name);
        return bundle;
    }

    public static StatusFragment newInstance(StatusPresenter.StatusGroup statusGroup, String uid, String name, boolean isRestore) {
        if(statusGroup.getStatusType() == StatusPresenter.StatusType.COMMENT_AT_ME ||
                statusGroup.getStatusType() == StatusPresenter.StatusType.TOPIC_AT_ME ||
        statusGroup.getStatusType() == StatusPresenter.StatusType.COMMENT_TO_ME ||
        statusGroup.getStatusType() == StatusPresenter.StatusType.COMMENT_BY_ME){

        }else{

        }

        StatusFragment fragment = new StatusFragment();
        fragment.setArguments(getBundle(statusGroup, isRestore, uid, name));
        return fragment;
    }

    public static StatusFragment newInstance(StatusPresenter.StatusGroup statusGroup) {
        return newInstance(statusGroup, false);
    }

    public static StatusFragment newInstance(StatusPresenter.StatusGroup statusGroup, boolean isRestore) {
        return newInstance(statusGroup, null, null, isRestore);
    }

//    public static StatusFragment newInstance(StatusPresenter.StatusType topicType, String uid, String name) {
//        return newInstance(new StatusPresenter.StatusGroup(topicType), uid, name, false);
//    }

    StatusPresenter mPresenter;
    StatusPresenter.StatusGroup statusGroup;
    String uid, name;
    boolean isRestore;

    StatusAdapterIdPager mStatusPager;

    @Override
    public StatusAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    protected void handleBundleExtra(Bundle bundle) {
        statusGroup = (StatusPresenter.StatusGroup) bundle.getSerializable("statusGroup");
        isRestore = bundle.getBoolean("isRestore");
        uid = bundle.getString("uid");
        name = bundle.getString("name");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setPager(mStatusPager = new StatusAdapterIdPager(mAdapter));
        setDataHandler();
        mAdapter.setShowDeleteStatusOption(true);

        mPresenter = new StatusPresenter(this, statusGroup);
        mPresenter.setUid(uid);
        mPresenter.setName(name);
        mAdapter.setStatusPresenter(mPresenter);

        if (isRestore && !UserUtil.isUserEmpty()) {
            onRestore();
        } else {
            showLoading();
            markLazyLoadPreparedAndLazyLoadIfCan();
        }
    }

    protected void setDataHandler(){
        setDataHandler(new StatusIdHandler<>(mAdapter));
    }

    private void onRestore() {
        ArrayList<Status> topic = mPresenter.findData();
        if (!CommonUtil.isEmpty(topic)) {
            int position = SPUtil.getInt("lastStatusPosition", 0);
            int offset = SPUtil.getInt("lastStatusOffset", 0);
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
            loadStatus();
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
        mPresenter.loadWBStatus(mStatusPager.getFirstPage(), true);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        mPresenter.loadWBStatus(mStatusPager.getNextPage(), false);
    }

    /**
     * 先检测有没有缓存，没有再请求服务器
     */
    public void loadStatus() {
        mPresenter.loadWBStatus(mStatusPager.getFirstPage());
    }

    @Override
    public void handleDataType(int type) {
        super.handleDataType(type);
        if (DataType.hasNewData(type)) {
            mPresenter.saveData(mAdapter.getData());
        }
    }

    @Override
    public void deleteStatusResult(int taskState, Status status) {
        if (TaskState.isSuccess(taskState)) {
            mAdapter.removeItem(status);
            ToastUtil.showToastSuccess(R.string.label_status_destroy_success);
            mPresenter.clearCache();
        } else {
            ToastUtil.showToast(TaskState.toString(taskState));
        }
    }
}
