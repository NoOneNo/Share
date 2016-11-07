package com.hengye.share.module.util.encapsulation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.hengye.share.R;
import com.hengye.share.module.util.encapsulation.base.StateLayoutManager;
import com.hengye.share.module.util.encapsulation.base.TaskCallBack;

import static com.hengye.share.module.util.encapsulation.base.StateLayoutManager.OnFindStateViewListener;
import static com.hengye.share.module.util.encapsulation.base.StateLayoutManager.STATE_CONTENT;
import static com.hengye.share.module.util.encapsulation.base.StateLayoutManager.STATE_EMPTY;
import static com.hengye.share.module.util.encapsulation.base.StateLayoutManager.STATE_LOADING;
import static com.hengye.share.module.util.encapsulation.base.StateLayoutManager.STATE_NO_NETWORK;
import static com.hengye.share.module.util.encapsulation.base.StateLayoutManager.STATE_SERVICE_ERROR;
import static com.hengye.share.module.util.encapsulation.base.TaskState.isFailByNetwork;
import static com.hengye.share.module.util.encapsulation.base.TaskState.isFailByServer;

/**
 * Created by yuhy on 16/7/18.
 */
public abstract class StateFragment extends BaseFragment
        implements OnFindStateViewListener, TaskCallBack{

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_content;
    }

    abstract public int getContentResId();

    public int getLoadingResId(){
        return R.layout.state_loading_center;
    }

    public int getEmptyResId(){
        return R.layout.state_empty_center;
    }

    public int getNoNetworkResId(){
        return R.layout.state_no_network_center;
    }

    public int getServiceErrorResId(){
        return R.layout.state_service_error_center;
    }

    /**
     * 如果为true, 则把getEmptyResId等当做layoutId添加进父view, 否则当做viewId查找获得相应的view;
     * @return
     */
    public boolean isLayoutInflateMode(){
        return true;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewGroup viewGroup;
        View content = view.findViewById(R.id.content);
        if(content != null && content instanceof ViewGroup) {
            viewGroup = (ViewGroup) content;
        }else if(view instanceof ViewGroup){
            viewGroup = (ViewGroup)view;
        }else{
            viewGroup = (ViewGroup) view.getParent();
        }
        initStateLayoutManager(viewGroup);
        showContent();
    }

    @Override
    public void onResume() {
        super.onResume();
        mStateLayoutManager.updateState();
    }

    StateLayoutManager mStateLayoutManager;

    private void initStateLayoutManager(ViewGroup viewGroup){
        mStateLayoutManager = new StateLayoutManager(getContext(), viewGroup, isLayoutInflateMode());
        mStateLayoutManager.setViewResourceId(STATE_EMPTY, getEmptyResId());
        mStateLayoutManager.setViewResourceId(STATE_CONTENT, getContentResId());
        mStateLayoutManager.setViewResourceId(STATE_LOADING, getLoadingResId());
        mStateLayoutManager.setViewResourceId(STATE_NO_NETWORK, getNoNetworkResId());
        mStateLayoutManager.setViewResourceId(STATE_SERVICE_ERROR, getServiceErrorResId());
        mStateLayoutManager.setOnFindStateViewListener(this);
    }

    @Override
    public void onFindStateView(View stateView, int state) {
        if(state == STATE_EMPTY
                || state == STATE_NO_NETWORK
                || state == STATE_SERVICE_ERROR){
            final View retry = stateView.findViewById(R.id.btn_retry);
            if(retry != null){
                retry.setOnClickListener(getOnClickRetryListener());
            }
        }
    }

    View.OnClickListener mOnClickRetryListener;
    private View.OnClickListener getOnClickRetryListener(){
        if(mOnClickRetryListener == null){
            mOnClickRetryListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRetry();
                }
            };
        }
        return mOnClickRetryListener;
    }

    @Override
    public void onTaskStart() {
        if(isEmpty()) {
            showLoading();
        }
    }

    @Override
    public void onTaskComplete(boolean isRefresh, int taskState) {
        if(isRefresh && isEmpty()){
            if(isFailByNetwork(taskState)) {
                showNoNetwork();
            }else if(isFailByServer(taskState)){
                showServiceError();
            }else{
                showEmpty();
            }
        }
    }

    public boolean isEmpty(){
        return true;
    }

    public void onRetry(){}

    public View getLoading() {
        return mStateLayoutManager.getStateView(STATE_LOADING);
    }

    public View getNoNetwork() {
        return mStateLayoutManager.getStateView(STATE_NO_NETWORK);
    }

    public View getServiceError() {
        return mStateLayoutManager.getStateView(STATE_SERVICE_ERROR);
    }

    public View getEmpty() {
        return mStateLayoutManager.getStateView(STATE_EMPTY);
    }

    public View getContent() {
        return mStateLayoutManager.getStateView(STATE_CONTENT);
    }

    public void showLoading() {
        mStateLayoutManager.showState(STATE_LOADING);
    }

    public void showNoNetwork() {
        mStateLayoutManager.showState(STATE_NO_NETWORK);
    }

    public void showServiceError() {
        mStateLayoutManager.showState(STATE_SERVICE_ERROR);
    }

    public void showEmpty() {
        mStateLayoutManager.showState(STATE_EMPTY);
    }

    public void showContent() {
        mStateLayoutManager.showState(STATE_CONTENT);
    }
}
