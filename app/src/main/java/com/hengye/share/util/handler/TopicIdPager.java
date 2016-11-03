package com.hengye.share.util.handler;

import com.hengye.share.module.util.encapsulation.base.Pager;
import com.hengye.share.model.TopicId;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.thirdparty.WBUtil;

import java.util.List;

/**
 * Created by yuhy on 16/7/28.
 */
public class TopicIdPager extends Pager {

    public TopicIdPager(List<? extends TopicId> adapter){
        mIds = adapter;
    }

    List<? extends TopicId> mIds;

    public void refreshIds(List<? extends TopicId> ids){
        this.mIds = ids;
    }

    @Override
    public String getFirstPage() {
        if (!CommonUtil.isEmpty(mIds)) {
            return mIds.get(0).getId();
        } else {
            return "0";
        }
    }

    @Override
    public String getNextPage() {
        if (!CommonUtil.isEmpty(mIds)) {
            long id = Long.valueOf(CommonUtil.getLastItem(mIds).getId());
            return String.valueOf(id - 1);

        }
        return null;
    }

    @Override
    public int getPageSize() {
        return WBUtil.getWBTopicRequestCount();
    }

    @Override
    public void handlePage(boolean isRefresh) {}
}
