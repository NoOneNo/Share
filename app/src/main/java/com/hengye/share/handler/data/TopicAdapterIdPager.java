package com.hengye.share.handler.data;

import com.hengye.share.handler.data.base.DataAdapter;
import com.hengye.share.handler.data.base.Pager;
import com.hengye.share.helper.SettingHelper;
import com.hengye.share.model.TopicId;
import com.hengye.share.util.CommonUtil;

/**
 * Created by yuhy on 16/7/28.
 */
public class TopicAdapterIdPager extends Pager {

    public TopicAdapterIdPager(DataAdapter<? extends TopicId> adapter){
        mAdapter = adapter;
    }

    DataAdapter<? extends TopicId> mAdapter;

    @Override
    public String getFirstPage() {
        if (!mAdapter.isEmpty()) {
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
    public void handlePage(boolean isRefresh) {}
}
