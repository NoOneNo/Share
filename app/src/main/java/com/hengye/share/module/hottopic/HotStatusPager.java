package com.hengye.share.module.hottopic;

import com.hengye.share.module.util.encapsulation.base.Pager;

/**
 * Created by yuhy on 2017/2/10.
 * 热门微博接口分页器
 * 第一页不传since_id，第二页传since_id=1，依次累加，不能设置每页请求的数量
 */

public class HotStatusPager extends Pager{

    private int pageNumber;

    public HotStatusPager(){
        pageNumber = 0;
    }

    @Override
    public Integer getFirstPage() {
        return pageNumber;
    }

    @Override
    public Integer getNextPage() {
        return pageNumber + 1;
    }

    @Override
    public void handlePage(boolean isRefresh) {
        pageNumber = isRefresh ? 0 : ++pageNumber;
    }

    @Override
    public int getPageSize() {
        return 0;
    }
}
