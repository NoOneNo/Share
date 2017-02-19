package com.hengye.share.util.handler;

import com.hengye.share.module.util.encapsulation.base.DataAdapter;
import com.hengye.share.module.util.encapsulation.base.Pager;
import com.hengye.share.model.StatusId;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.thirdparty.WBUtil;

/**
 * Created by yuhy on 16/7/28.
 */
public class StatusAdapterIdPager extends Pager {

    public StatusAdapterIdPager(DataAdapter<? extends StatusId> adapter){
        mAdapter = adapter;
    }

    DataAdapter<? extends StatusId> mAdapter;
    boolean mForceRefresh = false;

    @Override
    public String getFirstPage() {
        if (!mForceRefresh && !mAdapter.isEmpty()) {
            return mAdapter.getData().get(0).getId();
        } else {
            return "0";
        }
    }

    @Override
    public String getNextPage() {
        if (!mAdapter.isEmpty()) {
            long id = Long.valueOf(CommonUtil.getLastItem(mAdapter.getData()).getId());
            return String.valueOf(id - 1);

        }
        return null;
    }

    @Override
    public int getPageSize() {
        return WBUtil.getWBStatusRequestCount();
    }

    @Override
    public void handlePage(boolean isRefresh) {}

    public void setForceRefresh(boolean forceRefresh) {
        this.mForceRefresh = forceRefresh;
    }
}
