package com.hengye.share.module.util.encapsulation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hengye.share.module.util.encapsulation.base.DataHandler;
import com.hengye.share.module.util.encapsulation.base.ListDataCallBack;
import com.hengye.share.module.util.encapsulation.base.Pager;
import com.hengye.share.module.util.encapsulation.base.PagingConfig;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.ToastUtil;

import java.util.List;

import static com.hengye.share.module.util.encapsulation.base.DataType.LOAD_NO_DATA;
import static com.hengye.share.module.util.encapsulation.base.DataType.REFRESH_NO_DATA;
import static com.hengye.share.module.util.encapsulation.base.TaskState.isFailByNetwork;
import static com.hengye.share.module.util.encapsulation.base.TaskState.isFailByServer;

/**
 * Created by yuhy on 16/7/27.
 */
public abstract class ListDataFragment<T> extends StateFragment implements ListDataCallBack<T> {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPagingConfig = new PagingConfig();
        updatePagingConfig();
    }

    PagingConfig mPagingConfig;
    Pager mPager;
    DataHandler mDataHandler;

    public int handleData(boolean isRefresh, List<T> data){
        if(getPager() != null) {
            getPager().handlePage(isRefresh);
        }
        int type = -1;
        if(getDataHandler() != null) {
            type = getDataHandler().handleData(isRefresh, data);
            handleDataType(type);
        }
        getPagingConfig().setLoadEnable(canLoadMore(data, type));
        updatePagingConfig();
        return type;
    }

    public boolean canLoadMore(List<T> data, int type){
        return !CommonUtil.isEmpty(data) && type != LOAD_NO_DATA && type != REFRESH_NO_DATA;
    }

    public void handleDataType(int type) {
//        if(type != DataType.REFRESH_NO_DATA){
//            showContent();
//        }
    }

    private Pager getPager() {
        return mPager;
    }

    public void setPager(Pager pager) {
        this.mPager = pager;
    }

    private DataHandler getDataHandler() {
        return mDataHandler;
    }

    public void setDataHandler(DataHandler dataHandler) {
        this.mDataHandler = dataHandler;
    }

    public void updatePagingConfig(PagingConfig pagingConfig){}

    public PagingConfig getPagingConfig(){
        return mPagingConfig;
    }

    final protected void updatePagingConfig(){
        updatePagingConfig(mPagingConfig);
    }

    @Override
    public void onTaskStart() {
        if(isEmpty()) {
            showLoading();
        }
    }

    @Override
    public void onTaskComplete(boolean isRefresh, final int taskState) {
        if(isRefresh && isEmpty()){
            getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(isFailByNetwork(taskState)) {
                        showNoNetwork();
                    }else if(isFailByServer(taskState)){
                        showServiceError();
                    }else{
                        showEmpty();
                    }
                }
            }, 1000);

        }else{
            showContent();

            if(isFailByNetwork(taskState)) {
                ToastUtil.showNetWorkErrorToast();
            }else if(isFailByServer(taskState)){
                ToastUtil.showLoadErrorToast();
            }
        }
    }

    @Override
    public void onLoadListData(boolean isRefresh, List<T> data) {
        handleData(isRefresh, data);
    }
}










