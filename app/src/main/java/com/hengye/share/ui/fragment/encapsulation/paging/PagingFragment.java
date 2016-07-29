package com.hengye.share.ui.fragment.encapsulation.paging;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hengye.share.handler.data.base.DataHandler;
import com.hengye.share.handler.data.base.Pager;
import com.hengye.share.ui.fragment.encapsulation.ContentFragment;

import java.util.List;

/**
 * Created by yuhy on 16/7/27.
 */
public abstract class PagingFragment<T> extends ContentFragment{

    abstract public Pager getPager();

    abstract public DataHandler<T> getDataHandler();

    abstract public void updatePagingConfig(PagingConfig pagingConfig);

    void adjustPagingConfig(PagingConfig pagingConfig){}

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPagingConfig = new PagingConfig();
        adjustPagingConfig(mPagingConfig);
        updatePagingConfig();
    }

    PagingConfig mPagingConfig;

    public int handleData(boolean isRefresh, List<T> data){
        getPager().handlePage(isRefresh);
        int type = getDataHandler().handleData(isRefresh, data);
        handleDataType(type);
        updatePagingConfig();
        return type;
    }

    abstract public void handleDataType(int type);

    public PagingConfig getPagingConfig(){
        return mPagingConfig;
    }

    final protected void updatePagingConfig(){
        updatePagingConfig(mPagingConfig);
    }
}










