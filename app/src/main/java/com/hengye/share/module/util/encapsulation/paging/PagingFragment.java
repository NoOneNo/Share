package com.hengye.share.module.util.encapsulation.paging;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hengye.share.handler.data.base.DataHandler;
import com.hengye.share.handler.data.base.DataType;
import com.hengye.share.handler.data.base.Pager;
import com.hengye.share.module.util.encapsulation.ContentFragment;
import com.hengye.share.util.CommonUtil;

import java.util.List;

import static com.hengye.share.handler.data.base.DataType.*;
import static com.hengye.share.module.util.encapsulation.paging.TaskState.*;

/**
 * Created by yuhy on 16/7/27.
 */
public abstract class PagingFragment<T> extends ContentFragment implements ListDataCallBack<T>{

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPagingConfig = new PagingConfig();
        updatePagingConfig();
    }

    PagingConfig mPagingConfig;

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
        if(type == DataType.REFRESH_NO_DATA){
            showEmpty();
        }else{
            showContent();
        }
    }

    @Override
    public void onTaskStart() {
        showLoading();
    }

    @Override
    public void onTaskComplete(boolean isRefresh, int taskState) {
        if(isRefresh && isEmpty()){
            if(isFailByNetwork(taskState)) {
                showNoNetwork();
            }else if(isFailByServer(taskState)){
                //showServerError();
            }
        }
    }

    @Override
    public void onLoadListData(boolean isRefresh, List<T> data) {
        handleData(isRefresh, data);
    }

    public abstract boolean isEmpty();

    public Pager getPager(){
        return null;
    }

    public DataHandler<T> getDataHandler(){
        return null;
    }

    public void updatePagingConfig(PagingConfig pagingConfig){}

    public PagingConfig getPagingConfig(){
        return mPagingConfig;
    }

    final protected void updatePagingConfig(){
        updatePagingConfig(mPagingConfig);
    }
}










