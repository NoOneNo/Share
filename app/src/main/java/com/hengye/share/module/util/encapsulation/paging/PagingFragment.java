package com.hengye.share.module.util.encapsulation.paging;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hengye.share.handler.data.base.DataHandler;
import com.hengye.share.handler.data.base.Pager;
import com.hengye.share.module.util.encapsulation.ContentFragment;

import java.util.List;

import static com.hengye.share.handler.data.base.DataType.LOAD_NO_DATA;

/**
 * Created by yuhy on 16/7/27.
 */
public abstract class PagingFragment<T> extends ContentFragment{

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
        updatePagingConfig();
        return type;
    }

    public void handleDataType(int type){
        getPagingConfig().setLoadEnable(type != LOAD_NO_DATA);
    }

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










