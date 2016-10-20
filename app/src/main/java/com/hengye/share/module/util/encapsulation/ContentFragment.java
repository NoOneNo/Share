package com.hengye.share.module.util.encapsulation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.hengye.share.R;
import com.hengye.share.module.base.BaseFragment;

import static com.hengye.share.module.util.encapsulation.StateLayoutManager.*;

/**
 * Created by yuhy on 16/7/18.
 */
public abstract class ContentFragment extends BaseFragment implements StateLayoutManager.OnFindStateViewListener{

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_content;
    }

    abstract public int getContentResId();

    public int getLoadingResId(){
        return R.layout.widget_loading_center;
    }

    public int getEmptyResId(){
        return R.layout.widget_empty_center;
    }

    public int getNoNetworkResId(){
        return R.layout.widget_no_network_center;
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
        initContent(savedInstanceState);
    }

    StateLayoutManager mStateLayoutManager;

    private void initStateLayoutManager(ViewGroup viewGroup){
        mStateLayoutManager = new StateLayoutManager(getContext(), viewGroup, isLayoutInflateMode());
        mStateLayoutManager.setViewResourceId(STATE_EMPTY, getEmptyResId());
        mStateLayoutManager.setViewResourceId(STATE_CONTENT, getContentResId());
        mStateLayoutManager.setViewResourceId(STATE_LOADING, getLoadingResId());
        mStateLayoutManager.setViewResourceId(STATE_NO_NETWORK, getNoNetworkResId());
        mStateLayoutManager.setOnFindStateViewListener(this);
    }

    @Override
    public void onFindStateView(View stateView, int state) {
        if(state == STATE_EMPTY || state == STATE_NO_NETWORK){
            final View retry = stateView.findViewById(R.id.btn_retry);
            if(retry != null){
                retry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onRetry();
                    }
                });
            }
        }
    }

    public void onRetry(){}

    protected void initContent(@Nullable Bundle savedInstanceState){}

    public View getLoading() {
        return mStateLayoutManager.getStateView(STATE_LOADING);
    }

    public View getNoNetwork() {
        return mStateLayoutManager.getStateView(STATE_NO_NETWORK);
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

    public void showEmpty() {
        mStateLayoutManager.showState(STATE_EMPTY);
    }

    public void showContent() {
        mStateLayoutManager.showState(STATE_CONTENT);
    }
}
